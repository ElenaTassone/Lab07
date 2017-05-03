package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.dizionario.db.WordDAO;

public class Model {

	WordDAO dao ; 
	
	List<String> risultato = new ArrayList<String>();
	List<String> parole = new ArrayList<String>() ;
	
	UndirectedGraph<String,DefaultEdge> grafo = null;
	
	int max ;
	
	int uguale ;
	
	public List<String> createGraph(int numeroLettere) {
		grafo = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		dao = new WordDAO() ;
		List<String> parole = new ArrayList<String>() ;
		List<String> parole2 =new ArrayList<String>() ;
		List<String> vertici =new ArrayList<String>() ;
		parole = dao.getAllWordsFixedLength(numeroLettere) ;
		for(String p : parole){
			if(!grafo.containsVertex(p)){
				grafo.addVertex(p) ;
				vertici.add(p) ;
				}
			}
		for(String p : parole){
			 parole2 = this.trovaSimili(p, parole);
//			parole2 = dao.getAllSimilarWords(p, numeroLettere);
			for(String p2 : parole2){
				if(p2.compareTo(p)!=0){
					if(!grafo.containsVertex(p2)){
						grafo.addVertex(p2) ;
						}
					grafo.addEdge(p, p2) ;
				 
				}
			}
		}
		return vertici ;
	}

	public List<String> trovaSimili(String p, List<String> parole) {
		 List<String> risultato = new  LinkedList<String> () ;
		 for(String parola : parole){
			uguale = 0 ;
			int i = 0 ;
			while(i<p.length()){
				if(p.charAt(i) == parola.charAt(i))
					uguale++ ;
				i++ ;
					

			}
			i=0;
			
			if(uguale==p.length()-1)
				risultato.add(parola);
			uguale = 0 ;
		 	}
		 return risultato ;
			
//			char [] arr1 = p.toCharArray() ;
//			char [] arr2 = parola.toCharArray() ;
//			for(int i = 0 ; i < arr1.length ; i ++){
//				
//			}
		
	}

	public List<String> displayAllNeighbours1(String parolaInserita) {
		List<String> parole = new ArrayList<String>();
		BreadthFirstIterator<String, DefaultEdge> bfv = new BreadthFirstIterator<> (grafo, parolaInserita) ;
		while(bfv.hasNext()){
			parole.add(bfv.next()) ;
			}		
		return parole;
	}

	public List<String> displayAllNeighbours2(String parolaInserita) {
		this.recursive(parolaInserita);
		return risultato ;
		}
	
	private void recursive(String parola) {
		
		if(Graphs.neighborListOf(grafo, parola).size()==0) {
			return;
		}
		parole = Graphs.neighborListOf(grafo, parola) ;
		for(String s : parole){
			if(!risultato.contains(s)){
				risultato.add(s) ;
				this.recursive(s);
			}
			
			}
		return ;
		
		
	}
	
	public String findMaxDegree() {
		//Set<String> vertici = grafo.vertexSet() ;
		max = 0 ;
		String parola = null ;
		for(String s : grafo.vertexSet()){
			if(grafo.degreeOf(s)>max){
				max = grafo.degreeOf(s) ;
				parola = s ;
			}
		}
			
		return parola+" il cui grado max è"+max;
	}

	public List<String> displayNeighbours(String parola) {
		return Graphs.neighborListOf(grafo, parola) ;
	}

//	public List<String> displayAllNeighbours(String parola) {
//		dao = new WordDAO() ;
//		List<String> parole = new ArrayList<String>();
//		parole = dao.getAllSimilarWords(parola, parola.length());
//		
//		return null;
//	}
}
