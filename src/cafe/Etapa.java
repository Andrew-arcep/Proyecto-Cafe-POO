package cafe;

public interface Etapa {
    boolean validarRequisitos(Lote l, Colaborador c, Bodega b);
    void ejecutar(Lote l, Colaborador c, Bodega b);
}
