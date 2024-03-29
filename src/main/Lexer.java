package main;

import java.util.ArrayList;

public class Lexer {
	
	public String salida;
	private static final String[] COMLEX = {"(class|if|while|static|void)","[{|}|\\;|=|(|)]","(>|<|>=|<=|==|!=)","(\\+|-|/|\\*)",
			"(boolean|int|String|double|float)","(public|private)",/*"[1-9]?[0-9]"*/"[0-9]{1,3}(\\.[0-9]{1,3}f?)?","(true|false)",
			"\".*\"",/*"[0-9]{1,3}\\.[0-9]{1,3}f",*/"[a-z|_]+"};
	private ArrayList<Token> arr = new ArrayList<>();
	private boolean Bandera = true;

	public boolean lexico(char[] carac){
		Bandera = true;
		salida = "";
		arr.clear();				//Limpia el Arragloe de los tokens
		String token = "";
		boolean noError = false;		//Variable que controla los noErrores
		int columna = 0, linea = 1;	//Varibbles que cuentan la linea y la columna
		for (int i = 0; i < carac.length; i++) {	//REcorremos la cadena
			 //char car = cad.charAt(i);				//Tomamos el caracter segun la posicion
			char car = carac[i];
			 if(Character.isLetterOrDigit(car)){	// El caracter es un numero o digito?
				 token += car;				//Concatenamos el caracter en el token
				 int j = i + 1;				//Avanzamos al siguiente caracter
				 columna++;					//Incrementamos la columna
				 try {
					 while(Character.isLetterOrDigit(carac[j]) || carac[j] == '.'){		// mientras el siguente caracter sea digito o letra
						 token += carac[j];		//concatenamos el caracter
						 j++;							//incrementamos el contador
						 columna++;						//incrementamos el no. de columna
						 if(j == carac.length) break;	//en caso de que el indice sea igual que el tama�o de la cadena; salimos del ciclo interno
					 }
				} catch (ArrayIndexOutOfBoundsException e) { }
				 i = j-1;					//regresamos el contador
			 }else if( isOperator(car)){	//pregunta si el caracter es un operador
				 token += car;		//concatenamos
				 int j = i + 1;		//
				 columna++;
				 try {
					 while(isOperator(carac[j])){
						 token += carac[j];
						 j++;
						 columna++;
						 if(j == carac.length) break;
					 }
				} catch (ArrayIndexOutOfBoundsException e) { }
				 i = j-1;
			 }else if( car == '\"'){			//
				 token += car;
				 int j = i + 1;
				 car = carac[j];
				 try {
					while(car != '\"'){
						token += car;
						j++;
						car = carac[j];
					}
				 }catch (StringIndexOutOfBoundsException e) { }
				 token += car;
				 i = j;
			 }else if(String.valueOf(car).matches("\\S")){	//el caracter de cualquier caracter que no sea un espacion en blanco,un tabulador, un retorno de carro o un salto de linea
				 token += car;		//concatenamos el caracter
				 columna++;
			 }else if(car == '\n' ){	// es un salto de linea
				 linea++;		//incrementamos la linea
				 noError = true;	//media para que el caracter no sea tomado como uno no valido
				 columna = 0;
			 }else if( car == '\t' || car == '\r' || car == ' '){
				 noError = true;    //media para que el caracter no sea tomado como uno no valido
			 }
			 //cliclo que verifica que el token cuadre con los tokens dados por la gramatica
			 for (int j = 0; j < COMLEX.length; j++) {
				if(token.matches(COMLEX[j])){
					arr.add(new Token(j, token,columna,linea));
					//componentes.insertar(new Token(j, token, columna, linea));
					noError = true;  //media para que el token no sea tomado como uno no valido
					break;
				}
			}
			 
			if(noError == false){ //en caso de que el token sea uno no valido
				if(car != '\n' || car != '\r' || car != '\t' || car != ' ')// se descarta la opcion de que sea un salto de liena o un retorno de carro o un tabulador o un espacion en blanco 
					//muestra la salida de los errores por consola
					//salida += "\tError en el token \""+token+"\" en la posicion Columna: "+columna+"\t Linea: "+linea+"\n";
					salida += "\tError Lexico Columna:"+columna+" Linea: "+linea+", el token \""+token+"\" no es aceptado y/o soportado\n";
			}
			 token = "";			//vaciamos el token
			 noError = false;		//lo devolvemos como al inicio
		}
		arr.add(new Token(Token.EOF, "\uffff", columna, linea));
		if(!salida.equals("")) Bandera = false;
		return Bandera;
	}
	private static boolean isOperator(char o){
		switch(o){
			case '+' : return true;
			case '-' : return true;
			case '/' : return true;
			case '*' : return true;
			case '<' : return true;
			case '>' : return true;
			case '=' : return true;		
		}
		return false;
	}
	public String retMsg(){
		return salida;
	}
	public ArrayList<Token> retComp(){
		return arr;
	}
}
