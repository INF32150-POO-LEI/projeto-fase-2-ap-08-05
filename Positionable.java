 

/**
 * Representa um objeto que tem posição na grelha
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public abstract class Positionable {
    /**
     * Grelha a que pertence
     */
    protected Grid grid;

    /**
     * Construtor de Positionable
     * @param grid Grelha a que pertence
     * @param position Posição inicial
     */
    public Positionable(Grid grid, Vector position) {
     if(grid == null) throw new DistributionCenterException(ErrorCode.POSITIONABLE_GRID_IS_NULL);
     if(position == null) throw new DistributionCenterException(ErrorCode.POSITIONABLE_POSITION_IS_NULL);
     boolean success = grid.addPositionable(this, position);
     if(!success) throw new DistributionCenterException(ErrorCode.POSITIONABLE_POSITION_IS_UNAVAILABLE);
     this.grid = grid;
    }

    /**
     * Retorna a grelha a que pertence
     * @return Grelha a que pertence
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Retorna a posição que este objeto ocupa na grelha
     * @return Vetor da posição deste objeto
     */
    public Vector getPosition() {
        return grid.getPosition(this);
    }

    /**
     * Tenta definir a posição deste objeto na grelha para a posição fornecida
     * @param position posição a definir
     * @return Verdadeiro caso este objeto tenha sido movido com sucesso
     */
    public boolean setPosition(Vector position) {
        if(position == null) return false;
        return grid.movePositionable(this, position);
    }
}
