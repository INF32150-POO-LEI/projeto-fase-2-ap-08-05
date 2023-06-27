package Classes;

import java.util.ArrayList;

/**
 * Representa uma pallet
 *
 * @author 202200196
 * @author 202200215
 *
 * @version 1.0
 */
public class Pallet {
    private static int COUNT = 0;
    private int id;

    private ArrayList<CardboardBox> cardboardBoxes;

    /**
     * Construtor da Pallet
     */
    public Pallet() {
        id = COUNT++;
        cardboardBoxes = new ArrayList<>();
    }

    /**
     * Retorna as caixas de cartão
     * @return ArrayList de caixas de cartão
     */
    public ArrayList<CardboardBox> getCardboardBoxes() { return cardboardBoxes; }

    /**
     * Retorna a caixa de cartão com o índice fornecido, caso exista
     * @param index índice da caixa
     * @return Caixa de cartão com o índice fornecido, caso exista, caso contrário, retorna null
     */
    public CardboardBox getCardboardBox(int index) {
        if (index < 0 || index >= cardboardBoxes.size()) return null;
        return cardboardBoxes.get(index);
    }

    /**
     * Tenta adicionar uma caixa
     * @param cardboardBox caixa a adicionar
     * @return Verdadeiro caso bem sucedido
     */
    public boolean addBox(CardboardBox cardboardBox) {
        if (cardboardBox == null || cardboardBoxes.contains(cardboardBox)) return false;
        return cardboardBoxes.add(cardboardBox);
    }

    /**
     * Tenta remover a caixa fornecida
     * @param cardboardBox caixa a remover
     * @return Verdadeiro caso exista e tenha sido removida
     */
    public boolean removeBox(CardboardBox cardboardBox) {
        if (!cardboardBoxes.contains(cardboardBox)) return false;
        return cardboardBoxes.remove(cardboardBox);
    }

    /**
     * Retorna o peso total da caixa de cartão
     * @return Peso total da caixa de cartão
     */
    public float getWeight() {
        float total = 0;
        for (CardboardBox cardboardBox : cardboardBoxes) { total += cardboardBox.getWeight(); }
        return total;
    }

    @Override
    public String toString() {
        return "PLT" + id;
    }
}
