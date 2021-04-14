package model.logic;

import java.util.Comparator;

import org.apache.commons.csv.CSVRecord;
import org.junit.experimental.categories.Categories;

import model.data_structures.ILista;
import model.data_structures.ITablaHash;
import model.data_structures.ListaEncadenada;
import model.data_structures.SeparateChainingHash;
import model.utils.Ordenamiento;

public class Country implements Comparable<Country> {
	private String name;
	private SeparateChainingHash<Integer, YoutubeVideo> videosbyTrending;
	private SeparateChainingHash<Integer, YoutubeVideo> videosbyLike;
	private SeparateChainingHash<Integer, Category> categories;
	private ILista<Tag> tags;
	private Ordenamiento<Integer, YoutubeVideo> sorter;

	public Country(String name, String videoTags) {
		this.name = name;
		videosbyLike = new SeparateChainingHash<Integer, YoutubeVideo>(1000);
		videosbyTrending = new SeparateChainingHash<Integer, YoutubeVideo>(1000);
		tags = new ListaEncadenada<Tag>();
		sorter = new Ordenamiento<Integer, YoutubeVideo>();
		categories = new SeparateChainingHash<Integer, Category>(100);
	}


	public String getName() {
		return name;
	}

	public ILista<YoutubeVideo> getTag(String tag) {
		int pos = tags.isPresent(new Tag(tag));
		return tags.getElement(pos).getVideos();
	}

	public void addToLists(YoutubeVideo yt) {
		videosbyLike.put(yt.getCategory_id(), yt);
		videosbyTrending.put(yt.getCategory_id(), yt);
		Category newCategory = new Category("" + yt.getCategory_id(), "");
		
		
		;
		Category categoryObj = categories.get(newCategory.getId());
		if (categoryObj == null) {
			categories.put(newCategory.getId(), newCategory);;
			categoryObj = newCategory;
		}
		categoryObj.addToLists(yt);
		String[] tagsArray = yt.getTags();
		for (String tag : tagsArray) {
			Tag newTag = new Tag(tag);
			int tagPos = tags.isPresent(newTag);
			Tag tagObj = tags.getElement(tagPos);
			if (tagObj == null) {
				tags.addLast(newTag);
				tagObj = newTag;
			}
			tagObj.addToLists(yt);
		}
	}
	
	public void orderLists() {
		Comparator<YoutubeVideo> comparatorLikes = new YoutubeVideo.ComparadorXLikes();
		Comparator<YoutubeVideo> comparatorTrending = new YoutubeVideo.ComparadorXTrending();
		sorter.quickSort(videosbyLike, comparatorLikes, false);
		sorter.quickSort(videosbyTrending, comparatorTrending, false);
		for (int i = 0; i < tags.size(); i++) {
			tags.getElement(i).orderLists();
		}
		for (int i = 0; i < categories.getSize(); i++) {
			categories.get((Integer) categories.getTable()[i].getKey()).orderLists();
		}
	}

	@Override
	public int compareTo(Country other) {
		return name.compareToIgnoreCase(other.getName());
	}

	@Override
	public String toString() {
		return name + "\n" + videosbyLike;
	}

	public SeparateChainingHash<Integer, YoutubeVideo> getCategoryViews(int categoryId) {
		
		if (categories.get(categoryId)==null) return new SeparateChainingHash<Integer, YoutubeVideo> (100);
		
		
		
		return  categories.get(categoryId).getVideosViews();
	}
	
	

	


	public ILista<YoutubeVideo> getTagViews(String tag) {
		int pos = tags.isPresent(new Tag(tag));
		if (pos < 0) return new ListaEncadenada<YoutubeVideo>();
		return tags.getElement(pos).getVideos();
	}

	public SeparateChainingHash<Integer, YoutubeVideo> getVideosbyTrending() {
		return videosbyTrending;
	}

	public String getCategories() {
		return categories.toString();
	}
}
