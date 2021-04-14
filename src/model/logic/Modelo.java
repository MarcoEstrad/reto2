package model.logic;

import java.io.FileReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.function.Predicate;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.ILista;
import model.data_structures.ListaEncadenada;
import model.data_structures.SeparateChainingHash;
import model.data_structures.SeparateChainingHash.Nodo;
import model.utils.Ordenamiento;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**V
	 * Atributos del modelo del mundo
	 */
	//private ILista<YoutubeVideo> orderedByLike;
	//private ILista<YoutubeVideo> orderedByViews;
	private SeparateChainingHash<Integer, Country> countries;
	private SeparateChainingHash<Integer, Category > categories;
	private SeparateChainingHash<Integer, YoutubeVideo> orderedByTrending;
	private SeparateChainingHash<Integer, YoutubeVideo> orderedByLike;
	private SeparateChainingHash<Integer, YoutubeVideo> orderedByViews;
	/**
	 * Atributos del modelo del mundo
	 */


	/**
	 * Constructor del modelo del mundo con capacidad predefinida
	 */
	public Modelo() {
	
		orderedByTrending = new SeparateChainingHash<Integer, YoutubeVideo>(100);
		orderedByLike = new SeparateChainingHash<Integer, YoutubeVideo> (100);
		orderedByViews = new SeparateChainingHash<Integer, YoutubeVideo>(100);
		categories = new SeparateChainingHash<Integer, Category>(100);
		countries = new SeparateChainingHash<Integer, Country>(100);
		// datos = new ListaEncadenada<YoutubeVideo>();
		cargar();
	}

	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo
	 * 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano() {
		return orderedByLike.getSize();
	}

	/**
	 * Requerimiento de agregar dato
	 * 
	 * @param dato
	 */
	public void agregar(YoutubeVideo dato) {
		orderedByTrending.put(Integer.parseInt(dato.getVideo_id()), dato);
	}

	

	public void cargar() {
		System.out.println("Start upload");
		Reader in;
		long start = System.currentTimeMillis();
		try {
			in = new FileReader("./data/category-id.csv");
			Iterable<CSVRecord> categoriesCsv = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : categoriesCsv) {
				String id = record.get(0);
				String name = record.get(1);
				Category category = new Category(id, name);
				categories.put(Integer.parseInt(id), category);;
			}
			in = new FileReader("./data/videos-small.csv");
			Iterable<CSVRecord> videosCsv = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : videosCsv) {
				String trending_date = record.get(1);
				String video_id = record.get("video_id");
				String title = record.get(2);
				String channel_title = record.get(3);
				String category_id = record.get(4);
				String publish_time = record.get(5);
				String videoTags = record.get(6);
				String views = record.get(7);
				String likes = record.get(8);
				String dislikes = record.get(9);
				String comment_count = record.get(10);
				String thumbnail_link = record.get(11);
				String comments_disabled = record.get(12);
				String ratings_disabled = record.get(13);
				String video_error_or_removed = record.get(14);
				String descriptio = record.get(15);
				String country = record.get(16);
				YoutubeVideo video = new YoutubeVideo(video_id, trending_date, title, channel_title, category_id,
						publish_time, videoTags, views, likes, dislikes, comment_count, thumbnail_link,
						comments_disabled, ratings_disabled, video_error_or_removed, descriptio, country);
				Country newCountry = new Country(country, videoTags);
				
				Country countryObj = countries.get(Integer.parseInt(newCountry.getName()));
				if (countryObj == null) {
					int key = Integer.parseInt(newCountry.getName());
					countries.put( key,newCountry );
					countryObj = newCountry;
				}
				Category newCategory = new Category(category_id, "");
				
				Category categoryObj = categories.get(Integer.parseInt(category_id));
				if (categoryObj == null) {
					throw new Error("El Id de categoría " + category_id + " no es un id valido");
				}
				countryObj.addToLists(video);
				categoryObj.addToLists(video);
				orderedByLike.put(Integer.parseInt(likes), video);
				orderedByViews.put(Integer.parseInt(views), video);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Creación: " + (end - start));
		System.out.println("size: " + orderedByLike.getSize() );
	}

	
	public String req1(String category_name, String country, int n) {
		int categoryId = -1;
		
		

		for (int i = 0; i < categories.getCapacidad(); i++) {
			
			
		  
			Category temp = categories.get ((Integer) categories.getTable()[i].getKey());
			if (temp.getName().trim().compareToIgnoreCase(category_name) == 0) {
				categoryId = temp.getId();
				break;
			}
		}
		Country newCountry = new Country(country, "");
		
		Country countryObj = countries.get(Integer.parseInt(newCountry.getName()));
		if (countryObj == null) {
			throw new Error("No se econtró país con nombre " + country);
		}
		
		
		
		
		String res = "trending_date" + "\t - \t" + "title" + "\t - \t" + "channel_title" + "\t - \t" + "publish_time"
				+ "\t - \t" + "views" + "\t - \t" + "likes" + "\t - \t" + "dislikes" + "\n";
		for (int i = 0; i < n; i++) {
			YoutubeVideo yt = countryObj.getCategoryViews(categoryId).get((Integer) countryObj.getCategoryViews(categoryId).getTable()[i].getKey());
			res += yt.getTrending_date().toString() + "\t" + yt.getTitle() + "\t" + yt.getChannel_title() + "\t"
					+ yt.getPublish_time() + "\t" + yt.getViews() + "\t" + yt.getLikes() + "\t" + yt.getDislikes()
					+ "\n";
		}
		return res;
	}

	
	public String req2(String country) {
		Country newCountry = new Country(country, "");
		
		Country countryObj = countries.get(Integer.parseInt(newCountry.getName()));;
		if (countryObj == null) {
			throw new Error("No se econtró país con nombre " + country);
		}
		Nodo[] resList = countryObj.getVideosbyTrending().getTable();
		String res = "title" + "\t - \t" + "channel_title" + "\t - \t" + "category_id" + "\t - \t" + "Días" + "\n";
		for (int i = 1; i < resList.length; i++) {
			YoutubeVideo yt = countryObj.getVideosbyTrending().get((Integer) resList[i].getKey());
			res += yt.getTitle() + "\t" + yt.getChannel_title() + "\t" + yt.getCategory_id() + "\t"
					+ yt.getTrendingDays() + "\n";
		}
		return res;
	}

	public String req3(String category_name) {
		Category category = null;
		for (int i = 0; i < categories.getSize(); i++) {
			Category temp = categories.get((Integer) categories.getTable()[i].getKey());
			if (temp.getName().trim().compareToIgnoreCase(category_name) == 0) {
				category = temp;
				break;
			}
		}
		if (category == null) {
			throw new Error("Categoría " + category_name + " No es valida");
		}
		Nodo[] resList = category.getVideosTrending().getTable();
		String res = "title" + "\t - \t" + "channel_title" + "\t - \t" + "category_id" + "\t - \t" + "Días" + "\n";
		for (int i = 1; i < resList.length; i++) {
			YoutubeVideo yt = category.getVideosTrending().get( (Integer)resList[i].getKey());
			res += yt.getTitle() + "\t\t" + yt.getChannel_title() + "\t" + yt.getCategory_id() + "\t"
					+ yt.getTrendingDays() + "\n";
		}
		return res;
	}

	public String req4( String tag) {
		Nodo[] n= countries.getTable();
		String res = "title" + "\t - \t" + "channel_title" + "\t - \t" + "publish_time" + "\t - \t" + "views"
				+ "\t - \t" + "likes" + "\t - \t" + "dislikes" + "\n";
     for (int i =0; i<n.length; i++) {
    	 
		Country newCountry = countries.get((Integer)n[i].getKey());
	
		
		ILista<YoutubeVideo> resList = newCountry.getTagViews(tag);
		
		for (int j = 0; j < resList.size(); j++) {
			YoutubeVideo yt = resList.getElement(j);
			res += yt.getTitle() + "\t" + yt.getChannel_title() + "\t" + yt.getPublish_time() + "\t" + yt.getViews()
					+ 
					"\t" + yt.getLikes() + "\t" + yt.getDislikes() + "\t" + yt.getTagsString() + "\n";
		}
     }
		return res;
	}

	@Override
	public String toString() {
		return countries.toString();
	}
}
