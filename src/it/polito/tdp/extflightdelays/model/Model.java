package it.polito.tdp.extflightdelays.model;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	ExtFlightDelaysDAO dao;
	Graph<Airport, DefaultWeightedEdge> grafo;
	Map<Integer, Airport> airportsIdMap;
	
	//ricorsione
	List<Airport> best;
	Integer tratteMax;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		airportsIdMap = new HashMap<>();
	}

	public void creaGrafo(Integer compagnieMin) {
		
		dao.loadAllAirports(airportsIdMap);
		grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		List<Rotta> rotte = new ArrayList<>(dao.getRotte(compagnieMin, airportsIdMap));
		for(Rotta r : rotte ) {
			grafo.addVertex(r.getSource());
			grafo.addVertex(r.getDestination());
		}
		
		for(Rotta r : rotte ) {
			DefaultWeightedEdge e = grafo.getEdge(r.getSource(), r.getDestination());
			
			if(e== null) {
				Graphs.addEdge(grafo, r.getSource(), r.getDestination(),r.getPeso());
			}
			else {
				double peso = grafo.getEdgeWeight(e)+r.getPeso();
				grafo.setEdgeWeight(e, peso);
			}
		}
	}
	
	public Set<Airport> getAeroporti(){
		return grafo.vertexSet();
	}

	public List<Vicino> getConnessi(Airport aeroporto) {
		List<Airport> connessi = new ArrayList<>(Graphs.neighborListOf(grafo, aeroporto));
		List<Vicino> result = new ArrayList<>();
		for(Airport a : connessi) {
			result.add(new Vicino( a, grafo.getEdgeWeight(grafo.getEdge(aeroporto, a))));
		}
		
		Collections.sort(result);
		return result;
	}

	public List<Airport> cercaPercorso(Airport partenza, Airport destinazione, Integer numeroTratte) {
		best = new ArrayList<>();
		List<Airport> parziale = new ArrayList<>();
		this.tratteMax = numeroTratte;
		parziale.add(partenza);
		best.add(partenza);
		
		
		this.recursive(1,parziale, destinazione);
		
		return best;
	}

	private void recursive(int livello, List<Airport> parziale, Airport destinazione) {
		if(parziale.size() == tratteMax) {
			if(raggiunge(parziale, destinazione) == true) {
				parziale.add( destinazione);
				if(this.getPeso(parziale)>this.getPeso(best)) {
					best = new ArrayList<>(parziale);
				}
			}
		}
		Airport ultimo = parziale.get(parziale.size()-1);
		List<Airport> vicini = Graphs.neighborListOf(grafo, ultimo);
		if(parziale.size()<tratteMax) {
		for(Airport a : vicini) {
			if(!parziale.contains(a) ){
				parziale.add(a);
				this.recursive(livello+1, parziale, destinazione);
				parziale.remove(parziale.size()-1);
			}
		}
		}
	}

	private boolean raggiunge(List<Airport> parziale, Airport destinazione) {
		Airport last = parziale.get(parziale.size()-1);
		Set<DefaultWeightedEdge> incoming = grafo.incomingEdgesOf(destinazione);
		for(DefaultWeightedEdge e : incoming) {
			if(grafo.getEdgeSource(e).equals(last)) {
				return true;
			}
		}
		return false;
	}

	public int getPeso(List<Airport> percorso) {
		double peso = 0;
		for(int i =0 ; i<percorso.size()-1; i++) {
			peso += grafo.getEdgeWeight(grafo.getEdge(percorso.get(i), percorso.get(i+1)));
		}
		return (int) peso;
	}

	
}
