package server.watchlist.views;

import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import jakarta.servlet.http.Cookie;
import server.watchlist.data.AnimeEntry;
import server.watchlist.data.DataHandler;
import server.watchlist.layouts.FiSoBar;
import server.watchlist.layouts.NavBar;
import server.watchlist.layouts.ResponseObjectLayout;
import server.watchlist.layouts.ResponseObjectLayoutList;
import server.watchlist.layouts.FilterModes;
import server.watchlist.layouts.SortingModes;
import server.watchlist.security.Auth;
import server.watchlist.security.Session;

@Route("/list")
public class ListView extends VerticalLayout implements Auth, ReloadableList {
	private ResponseObjectLayoutList responseObjectLayouts;
	private NavBar navBar;
	private FiSoBar fisoBar;
	private FilterModes[] filterModes = {};
	private SortingModes sortingMode = SortingModes.standard;
	private String titleFilter = "";
	private String noteFilter = "";
	private String genreFilter = "";
	private Paragraph entryCount;
	private Session session;
	
	public ListView() {
		DataHandler.refreshDetails();
		
		navBar = new NavBar();
		fisoBar = new FiSoBar(this);
		
		configureList();
		
		reAdd();
	}
	
	@Override
	public void setSession(Session s) {
		this.session = s;
	}
	
	@Override
	public void reAdd() {
		this.removeAll();
		this.add(navBar);
		this.add(fisoBar);
		responseObjectLayouts.addTo(this);
		entryCount = new Paragraph(responseObjectLayouts.size() + " entries shown");
		this.addComponentAtIndex(2, entryCount);
	}

	@Override
	public void configureList() {
		try {
			responseObjectLayouts = new ResponseObjectLayoutList(DataHandler.getUser(getUsername()));
		} catch (Exception e) {
			Notification.show("error");
			e.printStackTrace();
			return;
		}
		responseObjectLayouts.setQueryGenre(genreFilter);
		responseObjectLayouts.setQueryNote(noteFilter);
		responseObjectLayouts.setQueryTitle(titleFilter);
		responseObjectLayouts.setFilterModes(filterModes);
		responseObjectLayouts.setSortingMode(sortingMode);
	}

	@Override
	public void setFilterModes(FilterModes[] fm) {
		filterModes = fm;
	}

	@Override
	public void setSortingMode(SortingModes sm) {
		sortingMode = sm;
	}

	@Override
	public void titleFilter(String str) {
		titleFilter = str;
	}
	
	@Override
	public String titleFilter() {
		return titleFilter;
	}

	@Override
	public void noteFilter(String str) {
		noteFilter = str;
	}
	
	@Override
	public String noteFilter() {
		return noteFilter;
	}

	@Override
	public void genreFilter(String str) {
		genreFilter = str;
	}
	
	@Override
	public String genreFilter() {
		return genreFilter;
	}

	@Override
	public boolean isSelected(FilterModes fm) {
		for(FilterModes f : filterModes) {
			if(f == fm)
				return true;
		}
		return false;
	}

	@Override
	public SortingModes getSelected() {
		return sortingMode;
	}
}
