package com.example.buscaminas;

public class Casilla {
    public boolean destapado = false;
    public int contenido = 0;
    public int x, y, ancho;

    public boolean dentro(int x, int x1) {
        if (x >= this.x && x <= this.x + ancho && x1 >= this.y && x1 <= this.y + ancho) {
            return true;
        } else {
            return false;
        }
    }

    public void fijarxy(int i, int filaact, int anchocua) {
        this.x = i;
        this.y = filaact;
        this.ancho = anchocua;
    }

}
