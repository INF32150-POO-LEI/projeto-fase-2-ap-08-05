 

import java.util.ArrayList;

/**
 * Interface de todos os objetos que transportam carga
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public interface CargoTransporter<E> {
    /**
     * Retorna a capacidade do transportador de carga
     * @return Valor da capacidade
     */
    float getCapacity();

    /**
     * Retorna o peso total da carga
     * @return Peso total da carga
     */
    float getObjectsWeight();

    /**
     * Retorna todos os objetos da carga
     * @return Todos os objetos que pertecem à carga
     */
    ArrayList<E> getObjects();

    /**
     * Retorna, caso exista, o objeto da carga no índice fornecido
     * @param index índice do objeto
     * @return Objeto no índice fornecido, caso exista
     */
    E getObject(int index);

    /**
     * Adiciona um objeto à carga
     * @param object objeto a adicionar
     * @return Verdadeiro se o objeto foi adicionado
     */
    boolean addObject(E object);

    /**
     * Remove um objeto da carga
     * @param object objeto a remover
     * @return Verdadeiro se o objeto foi removido
     */
    boolean removeObject(E object);

}
