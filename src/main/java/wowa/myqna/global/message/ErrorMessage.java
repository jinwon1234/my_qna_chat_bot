package wowa.myqna.global.message;

public final class ErrorMessage {

    private static final String ERROR_PREFIX = "[ERROR] ";
    public static final String INVALID_CONSTRUCTOR = ERROR_PREFIX + "final 클래스는 인스턴스를 생성할 수 없습니다.";
    public static final String PDF_FILE_UPLOAD_ERROR =  ERROR_PREFIX + "PDF 파일 업로드 중 오류발생";
    public static final String NOT_FOUND_USER = ERROR_PREFIX + "존재하지 않는 회원입니다.";

    private ErrorMessage() {
        throw new AssertionError(INVALID_CONSTRUCTOR);
    }
}
