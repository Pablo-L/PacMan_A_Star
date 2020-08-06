package Juego;

import java.util.Date;
import java.util.Random;
import java.util.*;

//clase Aestrella
public class Aestrella {
    public boolean pacman;
    public int numeroFantasma = 0;

    //Camino
    char camino[][];

    //Casillas expandidas
    int camino_expandido[][];

    //Número de nodos expandidos
    int expandidos;

    //Coste del camino
    double coste_total;

//clase Nodo
    class Nodo {
        //variables miembro
        int h;//heuristica
        int g;//coste del movimiento
        int n;//columna
        int m;//fila
        Nodo padre;//corresponde al nodo padre       
        int movimiento;//indica el movimiento que se ha realizado para llegar a esa posición
        //Constructor vacio
        Nodo(){
            h = 0;
            g = 0;
            n = m = 0;
            padre = null;//cuidado con los nullpointerexception!!
            movimiento = 0;//cuidado no es un movimiento
        }
        //constructor de copia
        Nodo(Nodo copiar){
            h = copiar.h;
            g = copiar.g;
            n = copiar.n;
            m = copiar.m;
            padre.asigna(copiar.padre);
            movimiento = copiar.movimiento;
        }
        //para asignar a un nodo los valores de otro nodo
        public void asigna(Nodo asig){
          h = asig.h;
          g = asig.g;
          n = asig.n;
          m = asig.m;
          if(asig.padre != null){
            padre = asig.padre;
          }else{
            padre = null;
          }
          movimiento = asig.movimiento;
        }

        //método que devuelve el valor de f
        public int f(){
            return g + h;
        }
        //método para calcular la heurística y asignarla
        void calcH(int n, int m){
            /*//distancia euclídea
            this.h = (int) Math.sqrt((n - this.n)^2 +
                        (m - this.m)^2);*/
            //distancia de manhattan
            int res1, res2;
            res1 = this.n - n;
            res2 = this.m - m;
            if(res1 < 0){
                res1 = res1 * (-1);
            }
            if(res2 < 0){
                res2 = res2*(-1);
            }
            this.h = res1 + res2;
        }
        //devuelve un nodo correspondiente a desplazarse hacia arriba
        Nodo arriba(){
            Nodo nodo = new Nodo();
            nodo.h = 0;
            nodo.g = this.g+1;//nos hemos desplazado
            nodo.n = this.n;
            nodo.m  = this.m-1;
            nodo.padre = new Nodo();
            nodo.padre.asigna(this);
            nodo.movimiento = Laberinto.ARRIBA;
            return nodo;
        }
        //devuelve un nodo correspondiente a desplazarse hacia abajo
        Nodo abajo(){
            Nodo nodo = new Nodo();
            nodo.h = 0;
            nodo.g = this.g+1;
            nodo.n = this.n;
            nodo.m = this.m+1;
            nodo.padre = new Nodo();
            nodo.padre.asigna(this);
            nodo.movimiento = Laberinto.ABAJO;
            return nodo;
        }
        //devuelve un nodo correspondiente a desplazarse hacia la derecha
        Nodo derecha(){
            Nodo nodo = new Nodo();
            nodo.h = 0;
            nodo.g = this.g+1;
            nodo.n = this.n+1;
            nodo.m = this.m;
            nodo.padre = new Nodo();
            nodo.padre.asigna(this);
            nodo.movimiento = Laberinto.DERECHA;
            return nodo;
        }
        //devuelve un nodo correspondiente a desplazarse a la izquierda
        Nodo izquierda(){
            Nodo nodo = new Nodo();
            nodo.h = 0;
            nodo.g = this.g+1;
            nodo.n = this.n-1;
            nodo.m = this.m;
            nodo.padre = new Nodo();
            nodo.padre.asigna(this);
            nodo.movimiento = Laberinto.IZQUIERDA;
            return nodo;
        }
        //dos nodos serám iguales si tienen las mismas coordenadas
        public boolean iguales(Nodo otro) {
            return otro.n == this.n && otro.m == this.m;
        }
        public boolean iguales(int cn, int cm){
            return cn == this.n && cm == this.m;
        }
    }

    class ListaNodos{
        
        private ArrayList<Nodo> lista = new ArrayList<Nodo>();

        int indice(Nodo n){
            Iterator<Nodo> it = lista.iterator();
            int cont = 0;
            while(it.hasNext()){
                if(n.iguales(it.next())){
                    return cont;
                }
                cont++;
            }
            return cont;//error
        }
        //borra un nodo de la lista
        void remove(Nodo n){
            lista.remove(indice(n));
        }
        //añade un elemento a la lista
        void add(Nodo n){
            lista.add(n);
        }
        //obtiene la g para un nodo existente en la lista
        int getG(Nodo n){
            return lista.get(indice(n)).g;
        }
        //retorna un booleano en función de si el nodo esta en la lista o no
        boolean contiene(Nodo n){
            Iterator<Nodo> it = lista.iterator();
            while(it.hasNext()){
                if(n.iguales(it.next())){
                    return true;//encontrado
                }
            }
            return false;//no encontrado
        }
        boolean contiene(int n,int m){
            Iterator<Nodo> it = lista.iterator();
            Nodo nodo = new Nodo();
            while(it.hasNext()){
                nodo = it.next();
                if(n == nodo.n && m ==nodo.m){
                    return true;
                }
            }
            return false;
        }
        //retorna el nodo de menor f en la lista
        Nodo menor(){
            Nodo menor = new Nodo();
            Nodo nodoAux = new Nodo();
            Iterator<Nodo> it = lista.iterator();
            menor.asigna(it.next());
            while(it.hasNext()){
                nodoAux = it.next();
                if(nodoAux.f() < menor.f()){
                    menor.asigna(nodoAux);
                }
            }
            return menor;
        }
        //retorna un booleano en función de si la lista esta vacía o no
        boolean isEmpty(){
            return lista.isEmpty();
        }
        Iterator<Nodo> iterator(){
            return lista.iterator();
        }
        //retorna el número de nodos almacenados en la lista
        int size(){
            return lista.size();
        }
    }

    Aestrella(boolean esPacman){
    	pacman = esPacman;
    }

    void setNumFantasma(int num){
	numeroFantasma = num;
    }

    public int run(int controlador, Laberinto laberinto){
	int mov = 0;

	switch(controlador){
            case Juego.ALEATORIO:
			mov = aleatorio(laberinto);
			break;
            case Juego.AESTRELLA:
			mov = A(laberinto);
			break;
	}

	return mov;
    }

    // Genera un movimiento aleatorio a partir del mapa proporcionado
    int aleatorio(Laberinto laberinto){
       	boolean correcto = false;
	Random rnd = new Random();
	Date d = new Date();
	int mov = -1;

	rnd.setSeed(d.getTime());

	while (!correcto){
            Laberinto aux = new Laberinto(laberinto);

            mov = (int)(rnd.nextDouble() * 4) + 5;
            if (pacman){
		if (aux.moverPacman(mov) != -1)
                    correcto = true;
            }
            else {
                if (aux.moverFantasma(numeroFantasma,mov) != -1)
                    correcto = true;
            }
	}

	return mov;
    }

    // Genera un movimiento a partir del algoritmo A*
    int A(Laberinto laberinto){
	int mov = 0;

	if (pacman)
            mov = AestrellaPacman(laberinto);
        else
            mov = AestrellaFantasma(laberinto);

	return mov;
	}

    //Inicializa las matrices camino y camino expandido
    void inic (int tam){

        camino= new char[tam][tam];
        //Casillas expandidas
        camino_expandido= new int[tam][tam];

        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
        for(int i=0;i<tam;i++)
                for(int j=0;j<tam;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
        //Número de nodos expandidos
        expandidos=0;
    }

    //////////////////////////////
    // A ESTRELLA PARA FANTASMA //
    //////////////////////////////
    int AestrellaFantasma(Laberinto laberinto){
        int result=0; //Devuelve el movimiento a realizar
        boolean encontrado=false;

        inic(laberinto.tam());

        //AQUI ES DONDE SE DEBE IMPLEMENTAR A*
        ListaNodos listaInterior = new ListaNodos();
        ListaNodos listaFrontera = new ListaNodos();
        Nodo inicial = new Nodo();
        inicial.n = laberinto.obtenerPosicionFantasma(numeroFantasma)[0];
        inicial.m = laberinto.obtenerPosicionFantasma(numeroFantasma)[1];
        inicial.calcH(laberinto.obtenerPosicionPacman()[0],laberinto.obtenerPosicionPacman()[1]);
        listaFrontera.add(inicial);
        Nodo nodoAux = new Nodo();//nodo auxiliar
        while(!listaFrontera.isEmpty()){
            nodoAux.asigna(listaFrontera.menor());
            listaFrontera.remove(nodoAux);
            listaInterior.add(nodoAux);
            if(nodoAux.iguales(laberinto.obtenerPosicionPacman()[0]
                                ,laberinto.obtenerPosicionPacman()[1])){//meta
                encontrado = true;
                System.out.println("ENCONTRADO");
                coste_total = (double) nodoAux.g;
                expandidos = listaInterior.size();
                int paso = nodoAux.g;
                //encontrar el nodo inicial y saber que movimiento se ha realizado
                while(nodoAux.padre.padre != null){//el objetivo es encontrar el inicial
                    camino[nodoAux.m][nodoAux.n] = 'x';
                    camino_expandido[nodoAux.m][nodoAux.n] = paso;
                    paso--;
                    nodoAux.asigna(nodoAux.padre);
                }
                result = nodoAux.movimiento;
                camino[nodoAux.m][nodoAux.n] = 'x';
                camino_expandido[nodoAux.m][nodoAux.n] = paso;
                paso--;
                nodoAux.asigna(nodoAux.padre);
                camino[nodoAux.m][nodoAux.n] = 'x';
                camino_expandido[nodoAux.m][nodoAux.n] = paso;
                break;//para salir del bucle
            }
            //se crea un array de nodos con los 4 hijos del nodo actual
            Nodo hijos[] = new Nodo[4];
            hijos[0] = nodoAux.arriba();
            hijos[1] = nodoAux.abajo();
            hijos[2] = nodoAux.derecha();
            hijos[3] = nodoAux.izquierda();
            
            for(int i = 0; i < 4;i++){
                if(!listaInterior.contiene(hijos[i]) 
                    && laberinto.obtenerPosicion(hijos[i].n,hijos[i].m) != 1){
                    if(!listaFrontera.contiene(hijos[i])){
                        hijos[i].calcH(laberinto.obtenerPosicionPacman()[0],
                                        laberinto.obtenerPosicionPacman()[1]);
                        listaFrontera.add(hijos[i]);
                    }else if(hijos[i].g < listaFrontera.getG(hijos[i])){
                        //actualizar
                        listaFrontera.remove(hijos[i]); 
                        hijos[i].calcH(laberinto.obtenerPosicionPacman()[0],
                                        laberinto.obtenerPosicionPacman()[1]);
                        listaFrontera.add(hijos[i]);
                    }
                }
            }
        }
        //Si ha encontrado la solución, es decir, el camino, muestra las matrices camino y camino_expandidos y el número de nodos expandidos
        if(encontrado){
            //Mostrar la solucion
            System.out.println("NO MODIFICAR ESTE FORMATO DE SALIDA");
            System.out.println("Coste del camino: "+coste_total);
            System.out.println("Nodos expandidos: "+expandidos);
            System.out.println("Camino");
            mostrarCamino(camino, laberinto.tam());
            System.out.println("Camino explorado");
            mostrarCaminoExpandido(camino_expandido,laberinto.tam());
        }

        return result;

    }
    //clase que define un posible movimiento de pacman
    class movimientoPacman{

        int n;//columas
        int m;//filas
        int movimiento;//almacena el movimiento que se ha llevado a cabo para llegar a esa posición
        int coste;//coste del camino del fantasma mas cercano
        
        //constructor por defecto
        movimientoPacman(){
            n = m = 0;
            movimiento = 0;
            coste = 0;
        }
        //asigna un movimiento a otro
        void asignar(movimientoPacman asig){
            this.n = asig.n;
            this.m = asig.m;
            this.movimiento = asig.movimiento;
            this.coste = asig.coste;
        }
        //crea un movimiento correspopndiente a moverse hacia arriba
        void arriba(int n, int m){
            this.n = n;
            this.m = m-1;
            this.movimiento = Laberinto.ARRIBA;
            this.coste = 0;//habrá que calcular el coste 
        }
        //crea un movimiento correspondiente a moverse hacia abajo
        void abajo(int n, int m){
            this.n = n;
            this.m = m+1;
            this.movimiento = Laberinto.ABAJO;
            this.coste = 0;
        }
        //crea un movimiento correspondiente a moverse hacia la derecha
        void derecha(int n, int m){
            this.n = n+1;
            this.m = m;
            this.movimiento = Laberinto.DERECHA;
            this.coste = 0;
        }
        //crea un movimiento correspondiente a moverse hacia la izquierda
        void izquierda(int n, int m){
            this.n = n-1;
            this.m = m;
            this.movimiento = Laberinto.IZQUIERDA;
            this.coste = 0;
        }
        //utiliza el algoritmo A* entre el movimiento actual y una posición
        int Aestrella(int nFantasma, int mFantasma, Laberinto laberinto){
            ListaNodos listaInterior = new ListaNodos();
            ListaNodos listaFrontera = new ListaNodos();
            Nodo inicial = new Nodo();
            inicial.n = this.n;
            inicial.m = this.m;
            inicial.calcH(nFantasma, mFantasma);
            listaFrontera.add(inicial);
            Nodo nodoAux = new Nodo();
            while(!listaFrontera.isEmpty()){
                nodoAux.asigna(listaFrontera.menor());
                listaFrontera.remove(nodoAux);
                listaInterior.add(nodoAux);
                if(nodoAux.iguales(nFantasma,mFantasma)){//es meta
                    return nodoAux.g;
                }
                //creamos los cuatro hijos
                Nodo hijos[] = new Nodo[4];
                hijos[0] = nodoAux.arriba();
                hijos[1] = nodoAux.abajo();
                hijos[2] = nodoAux.izquierda();
                hijos[3] = nodoAux.derecha();
                for(int i = 0; i < 4; i++){
                    if(!listaInterior.contiene(hijos[i]) 
                        && laberinto.obtenerPosicion(hijos[i].n, hijos[i].m) != 1){
                        if(!listaFrontera.contiene(hijos[i])){
                            hijos[i].calcH(nFantasma,mFantasma);
                            listaFrontera.add(hijos[i]);
                        }else if(hijos[i].g < listaFrontera.getG(hijos[i])){
                            listaFrontera.remove(hijos[i]);
                            hijos[i].calcH(nFantasma,mFantasma);
                            listaFrontera.add(hijos[i]);//actualizar
                        }
                    }
                }
            }
            return 0;//error
        }
    }
    //////////////////////////////
    // A ESTRELLA PARA PACMAN //
    //////////////////////////////
    int AestrellaPacman(Laberinto laberinto){
	int result=0; //Devuelve el movimiento a realizar

        //AQUI ES DONDE SE DEBE IMPLEMENTAR EL CODIGO PARA PACMAN
        movimientoPacman movimientos[] = new movimientoPacman[4];
        for(int i = 0; i < 4; i++){
            movimientos[i] = new movimientoPacman();
        }
        movimientos[0].arriba(laberinto.obtenerPosicionPacman()[0],
                                laberinto.obtenerPosicionPacman()[1]);
        movimientos[1].abajo(laberinto.obtenerPosicionPacman()[0],
                                laberinto.obtenerPosicionPacman()[1]);
        movimientos[2].derecha(laberinto.obtenerPosicionPacman()[0],
                                laberinto.obtenerPosicionPacman()[1]);
        movimientos[3].izquierda(laberinto.obtenerPosicionPacman()[0],
                                  laberinto.obtenerPosicionPacman()[1]);
        movimientoPacman the_best = new movimientoPacman();
        for(int i = 0; i < 4; i++){//para cada movimiento
            if(laberinto.obtenerPosicion(movimientos[i].n, movimientos[i].m) != 1){//que sea accesible
                movimientos[i].coste = movimientos[i].Aestrella(laberinto.obtenerPosicionFantasma(1)[0],
                                                        laberinto.obtenerPosicionFantasma(1)[1],
                                                        laberinto);//calculamos el coste para el primer fantasma
                for(int j = 2; j <= laberinto.numFantasmas; j++){//para cada fantasma menos el primero
                    int costeNuevo = movimientos[i].Aestrella(laberinto.obtenerPosicionFantasma(j)[0],
                                                              laberinto.obtenerPosicionFantasma(j)[1],
                                                              laberinto);
                    if(costeNuevo < movimientos[i].coste){//se asignará el menor coste posible
                        movimientos[i].coste = costeNuevo;
                    }
                }
                if(i == 0 || the_best.coste < movimientos[i].coste){
                //si es la primera iteración o el coste del nuevo movimiento es mayor que el mejor coste
                    the_best.asignar(movimientos[i]);
                }
            } 
        }
        result = the_best.movimiento;
        return result;
    }


    //Muestra la matriz que contendrá el camino después de calcular A*
    public void mostrarCamino(char camino[][], int tam){
        for (int i=0; i<tam; i++){
            for(int j=0;j<tam; j++){
                System.out.print(camino[i][j]+" ");
            }
            System.out.println();
        }
    }

    //Muestra la matriz que contendrá el orden de los nodos expandidos despuÃ©s de calcular A*
    public void mostrarCaminoExpandido(int camino_exp[][], int tam){
        for (int i=0; i<tam; i++){
            for(int j=0;j<tam; j++){
                if(camino_exp[i][j]>-1 && camino_exp[i][j]<10)
                    System.out.print(" ");
                System.out.print(camino_exp[i][j]+" ");
            }
            System.out.println();
        }
    }
}
