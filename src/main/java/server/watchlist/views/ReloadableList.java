package server.watchlist.views;

import server.watchlist.layouts.FilterModes;
import server.watchlist.layouts.ResponseObjectLayoutList;
import server.watchlist.layouts.SortingModes;

public interface ReloadableList {
	public default void reloadList(FilterModes[] fm) {
		setFilterModes(fm);
		configureList();
		reAdd();
	}
	
	public default void reloadList(SortingModes sm) {
		setSortingMode(sm);
		configureList();
		reAdd();
	}
	
	void reAdd();
	
	void configureList();
	
	void setFilterModes(FilterModes[] fm);
	
	void setSortingMode(SortingModes sm);
	
	void titleFilter(String str);
	String titleFilter(); 
	
	void noteFilter(String str);
	String noteFilter();
	
	void genreFilter(String str);
	String genreFilter();
	
	boolean isSelected(FilterModes fm);
	
	SortingModes getSelected();
}
