package edoor.Data;

/**
 * Base exception for all exceptions in DataManagement
 * @author Geng Xuehong
 *
 */
public class DataException extends Throwable {

    /**
     * Serialization version ID
     */
    private static final long serialVersionUID = 1L;

    public DataException(String msg) {
        super(msg);
    }
}
