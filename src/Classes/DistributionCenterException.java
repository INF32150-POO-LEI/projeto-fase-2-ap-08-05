package Classes;

/**
 * Representa um erro do centro de distribuição
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class DistributionCenterException extends RuntimeException {
    /**
     * Código do erro
     */
    private ErrorCode code;

    /**
     * Construtor do DistributionCenterException
     * @param code código do erro
     */
    public DistributionCenterException(ErrorCode code) {
        super();
        this.code = validateCode(code);
    }

    private ErrorCode validateCode(ErrorCode code) {
        if(code == null) return ErrorCode.GENERIC;
        else return code;
    }

    /**
     * Retorna o código do erro
     * @return Código do erro
     */
    public ErrorCode getCode() {
        return code;
    }
}
