package model.logic;

import java.util.Comparator;

import org.junit.runner.manipulation.Sorter;

import model.data_structures.ILista;
import model.data_structures.ListaEncadenada;
import model.data_structures.SeparateChainingHash;
import model.utils.Ordenamiento;

public class Category implements Comparable<Category> {
	private int id;
	private String name;
	private SeparateChainingHash<Integer, YoutubeVideo> videosbyTrending;
	private SeparateChainingHash<Integer, YoutubeVideo> videosByViews;

	private Ordenamiento<Integer, YoutubeVideo> sorter;

	public Category(String id, String name) {
		this.name = name;
		this.id = Integer.parseInt(id);
		videosByViews = new SeparateChainingHash<Integer, YoutubeVideo>(1000) ;
		videosbyTrending = new SeparateChainingHash<Integer, YoutubeVideo>(1000);
		sorter = new Ordenamiento<Integer, YoutubeVideo>();
	}

	public void orderLists() {
		Comparator<YoutubeVideo> comparatorViews = new YoutubeVideo.ComparadorXViews();
		Comparator<YoutubeVideo> comparatorTrending = new YoutubeVideo.ComparadorXTrending();
		sorter.quickSort(videosByViews, comparatorViews, false);
		sorter.quickSort(videosbyTrending, comparatorTrending, false);
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public SeparateChainingHash<Integer,YoutubeVideo> getVideosViews() {
		return videosByViews;
	}

	public SeparateChainingHash<Integer,YoutubeVideo> getVideosTrending() {
		return videosbyTrending;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Category other) {
		if (id > other.getId())
			return 1;
		else if (id < other.getId())
			return -1;
		else
			return 0;
	}

	public void addToLists(YoutubeVideo yt) {
		videosByViews.put(yt.getCategory_id(), yt);
		videosbyTrending.put(yt.getCategory_id(), yt);
	}

	@Override
	public String toString() {
		return "Category: " + id + name + videosByViews;
	}
}
