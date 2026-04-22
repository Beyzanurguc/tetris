package core;

/**
 * FilledCell — Bir parçanın yerleşmiş olduğu dolu hücre.
 */
public class FilledCell extends Cell {

    public FilledCell(int colorId) {
        super(true, colorId);
    }

    @Override
    public boolean isFilled()  { return true; }

    @Override
    public int getColorId()    { return colorId; }

    @Override
    public void activate() {
        // Dolu hücrenin varsayılan davranışı: yok.
        // Örn.: ses efekti, animasyon tetikleyicisi buraya eklenebilir.
        System.out.println("[FilledCell] activate() — renk: " + colorId);
    }

    @Override
    public String render() {
        // Konsol görüntüsü için renk ID'yi köşeli parantez içinde göster
        return "[" + colorId + "]";
    }
}
