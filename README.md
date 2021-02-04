Buscaminas en Android Studio

Integrantes: 
 - Chantal Morales
 - Sebastián Morales
 - Kevin Segovia
 - Israel Vásquez
 - Nicole Zambrano

Crearemos un nuevo proyecto en Android Studio y ubicaremos dos elementos que van a ser el botón de reiniciar el juego y el layout en donde se mostraran las casillas.
Hecho eso, crearemos una nueva clase Java llamada `Casilla.java`, la cual contendrá lo siguiente:

```
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
```

Esta clase se encargará de las casillas dentro del layout.
Ahora en el `MainActivity.java` declararemos 3 variables las cuales serán de tipo `Tablero` (este será una clase declarada más adelante), `Casilla` (esta será declarada como un arrelgo) y un `boolean`.
Y dentro el método `onCreate()` deberá ser así

```
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);
        fondo = new Tablero(this);
        fondo.setOnTouchListener(this);
        layout.addView(fondo);
        casillas = new Casilla[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                casillas[f][c] = new Casilla();
            }
        }
        this.disponerBombas();
        this.contarBombasPerimetro();
        getSupportActionBar().hide();
    }
```
Lo que hará es inicializar el tablero llenando con casillas, además de ubicar las bombas de manera randómica y contar las bombas que se encuentran cerca de una casilla.
Ahora se procede a crear todos los metodos necesarios, invluyendo la clase `Tablero`

Método `reiniciar()`

Este método realizará lo mismo que el `onCreate()`, es decir, reestablecer el trablero. 

```
public void reiniciar(View view) {
        casillas = new Casilla[8][8];
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                casillas[f][c] = new Casilla();
            }
        }
        this.disponerBombas();
        this.contarBombasPerimetro();
        activo = true;
        fondo.invalidate();
    }
```
Método `onTouch()`

Este método verifica la acción de tocar una casilla y si en esa casilla se encuentra una bomba, y si es el caso mostrará un mensaje y terminará el juego.

```
 @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (activo)
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    if (casillas[f][c].dentro((int) event.getX(), (int) event.getY())) {
                        casillas[f][c].destapado = true;
                        if (casillas[f][c].contenido == 80) {
                            Toast.makeText(this, "Booooooooommmm!!!", Toast.LENGTH_LONG).show();
                            activo = false;
                        } else if (casillas[f][c].contenido == 0)
                            recorrer(f, c);
                        fondo.invalidate();
                    }
                }
            }
        if (gano() && activo) {
            Toast.makeText(this, "Ganaste!!", Toast.LENGTH_LONG).show();
            activo = false;
        }

        return true;
    }
```

Clase `Tablero`

Esta clase lo que hará es llenar el layout con las casillas y las bombas generadas, y pintar de un color unas u otras.

```
class Tablero extends View {

        public Tablero(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(0, 0, 0);
            int ancho = 0;
            if (canvas.getWidth() < canvas.getHeight()) {
                ancho = fondo.getWidth();
            } else {
                ancho = fondo.getHeight();
            }
            int anchocua = ancho / 8;
            Paint paint = new Paint();
            paint.setTextSize(20);
            Paint paint2 = new Paint();
            paint2.setTextSize(20);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255, 0, 0, 255);
            Paint paintlinea1 = new Paint();
            paintlinea1.setARGB(255, 255, 255, 255);
            int filaact = 0;
            for (int f = 0; f < 8; f++) {
                for (int c = 0; c < 8; c++) {
                    casillas[f][c].fijarxy(c * anchocua, filaact, anchocua);
                    if (casillas[f][c].destapado == false) {
                        paint.setARGB(153, 204, 204, 204);
                    } else {
                        paint.setARGB(255, 153, 153, 153);
                    }
                    canvas.drawRect(c * anchocua, filaact, c * anchocua + anchocua - 2, filaact + anchocua - 2, paint);
                    canvas.drawLine(c * anchocua, filaact, c * anchocua + anchocua, filaact, paintlinea1);
                    canvas.drawLine(c * anchocua + anchocua - 1, filaact, c * anchocua + anchocua - 1, filaact + anchocua,
                            paintlinea1);

                    if (casillas[f][c].contenido >= 1 && casillas[f][c].contenido <= 8 && casillas[f][c].destapado) {
                        canvas.drawText(String.valueOf(casillas[f][c].contenido), c * anchocua + (anchocua / 2) - 8, filaact + anchocua / 2, paint2);
                    }
                    if (casillas[f][c].contenido == 80 && casillas[f][c].destapado) {
                        Paint bomba = new Paint();
                        bomba.setARGB(255, 255, 0, 0);
                        canvas.drawCircle(c * anchocua + (anchocua / 2), filaact + (anchocua / 2), 8, bomba);
                    }
                }
                filaact = filaact + anchocua;
            }
        }
    }
```
Método `diponerBombas()`

Este método ubicará de manera random las bombas en las casillas para luego ser ubicadas en el tablero

```
private void disponerBombas() {
        int cantidad = 8;
        do {
            int fila = (int) (Math.random() * 8);
            int columna = (int) (Math.random() * 8);
            if (casillas[fila][columna].contenido == 0) {
                casillas[fila][columna].contenido = 80;
                cantidad--;
            }
        } while (cantidad != 0);
    }
```

Método `gano()`

Método que verifica si el numero de casillas destapadas es 56 de 80 para ganar el juego.

```
private boolean gano() {
        int cant = 0;
        for (int f = 0; f < 8; f++)
            for (int c = 0; c < 8; c++)
                if (casillas[f][c].destapado) {
                    cant++;
                }
        if (cant == 56) {
            return true;
        } else {
            return false;
        }
    }
```

Método `contarBombasPerimetro()`

Este método verifica cuantas bombas se encuentran en el perímetro de la casilla

```
private void contarBombasPerimetro() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 0) {
                    int cant = contarCoordenada(f, c);
                    casillas[f][c].contenido = cant;
                }
            }
        }
    }
```

Método `contarCoordenada()`

Este método devuelve el número total de bombas que se encuentran cerca de la casilla. 

```
int contarCoordenada(int fila, int columna) {
        int total = 0;
        if (fila - 1 >= 0 && columna - 1 >= 0) {
            if (casillas[fila - 1][columna - 1].contenido == 80) {
                total++;
            }
        }
        if (fila - 1 >= 0) {
            if (casillas[fila - 1][columna].contenido == 80) {
                total++;
            }
        }
        if (fila - 1 >= 0 && columna + 1 < 8) {
            if (casillas[fila - 1][columna + 1].contenido == 80) {
                total++;
            }
        }

        if (columna + 1 < 8) {
            if (casillas[fila][columna + 1].contenido == 80) {
                total++;
            }
        }
        if (fila + 1 < 8 && columna + 1 < 8) {
            if (casillas[fila + 1][columna + 1].contenido == 80) {
                total++;
            }
        }

        if (fila + 1 < 8) {
            if (casillas[fila + 1][columna].contenido == 80) {
                total++;
            }
        }
        if (fila + 1 < 8 && columna - 1 >= 0) {
            if (casillas[fila + 1][columna - 1].contenido == 80) {
                total++;
            }
        }
        if (columna - 1 >= 0) {
            if (casillas[fila][columna - 1].contenido == 80) {
                total++;
            }
        }
        return total;
    }
```

Método `recorrer()`

Método que como su nombre lo indica, recorre las casillas, verificando las destapadas y las que no.

```
private void recorrer(int fil, int col) {
        if (fil >= 0 && fil < 8 && col >= 0 && col < 8) {
            if (casillas[fil][col].contenido == 0) {
                casillas[fil][col].destapado = true;
                casillas[fil][col].contenido = 50;
                recorrer(fil, col + 1);
                recorrer(fil, col - 1);
                recorrer(fil + 1, col);
                recorrer(fil - 1, col);
                recorrer(fil - 1, col - 1);
                recorrer(fil - 1, col + 1);
                recorrer(fil + 1, col + 1);
                recorrer(fil + 1, col - 1);
            } else if (casillas[fil][col].contenido >= 1 && casillas[fil][col].contenido <= 8) {
                casillas[fil][col].destapado = true;
            }
        }
    }
```

Ahora, una vez implementados estos métodos, la aplicación se verá de la siguiente manera:

![Alt text](/captures/interfaz.png?raw=true "Interfaz")

En funcionamiento se verá así:

![Alt text](/captures/funcionamiento.png?raw=true "Funcionamiento")

Y para el fin de juego: 

![Alt text](/captures/fin_de_juego.png?raw=true "Fin de juego")
