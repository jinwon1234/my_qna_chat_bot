package wowa.myqna.embedding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wowa.myqna.embedding.dto.EmbeddingRequestDto;
import wowa.myqna.embedding.dto.EmbeddingResponseDto;
import wowa.myqna.global.exception.ApplicationCustomException;
import wowa.myqna.user.domain.UserEntity;
import wowa.myqna.user.service.UserLowService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static wowa.myqna.global.message.ErrorMessage.*;

@Transactional
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    
    private final VectorStore vectorStore;
    private final PdfDocumentReaderConfig pdfDocumentReaderConfig;
    private final UserLowService userLowService;
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB


    public EmbeddingResponseDto embeddingPdf(EmbeddingRequestDto myQnaRequestDto) {

        MultipartFile file = myQnaRequestDto.multipartFile();
        String username = myQnaRequestDto.myName();

        List<Document> chunks = getDocuments(file);

        String uuid = UUID.randomUUID().toString();

        chunks.forEach(doc -> doc.getMetadata().put("owner", uuid));

        vectorStore.accept(chunks);

        userLowService.save(new UserEntity(uuid, username));

        return new EmbeddingResponseDto(uuid, username);
    }

    private List<Document> getDocuments(MultipartFile file)  {
        try {
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new ApplicationCustomException(EXCEED_MAX_FILE_SIZE);
            }

            Resource resource = new InputStreamResource(file.getInputStream());
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, pdfDocumentReaderConfig);

            List<Document> documents = pdfReader.get();

            TokenTextSplitter splitter = new TokenTextSplitter(500, 200, 50, 5000, true);
            List<Document> chunks = splitter.apply(documents);
            return chunks;
        } catch (IOException e) {
            throw new ApplicationCustomException(PDF_FILE_UPLOAD_ERROR);
        }
    }


}
