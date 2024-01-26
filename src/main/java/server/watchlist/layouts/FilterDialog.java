package server.watchlist.layouts;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import server.watchlist.views.ReloadableList;

public class FilterDialog extends Dialog {
	private Checkbox[] boxes;
	private FilterModes[] filterModes = FilterModes.values();
	private ReloadableList reloadableList;
	private TextField titleFilter;
	private TextField noteFilter;
	private TextField genreFilter;
	
	public FilterDialog(ReloadableList reloadableList) {
		super();
		this.reloadableList = reloadableList;
		this.setHeaderTitle("Filters");
		this.addDialogCloseActionListener(e -> closeDialog());
		boxes = new Checkbox[filterModes.length];
		for(int i = 0; i < filterModes.length; i++) {
			boxes[i] = new Checkbox();
			boxes[i].setLabel(filterModes[i].toString());
			if(reloadableList.isSelected(filterModes[i]))
				boxes[i].setValue(true);
		}
		
		titleFilter = new TextField("Title filter");
		titleFilter.setValue(reloadableList.titleFilter());
		titleFilter.addValueChangeListener(e -> setTitleFilter());
		noteFilter = new TextField("Note filter");
		noteFilter.setValue(reloadableList.noteFilter());
		noteFilter.addValueChangeListener(e -> setNoteFilter());
		genreFilter = new TextField("Genre filter");
		genreFilter.setValue(reloadableList.genreFilter());
		genreFilter.addValueChangeListener(e -> setGenreFilter());
		
		VerticalLayout vl = new VerticalLayout();
		vl.add(boxes);
		vl.add(titleFilter, noteFilter, genreFilter);
		this.add(vl);
	}
	
	private void setTitleFilter() {
		reloadableList.titleFilter(titleFilter.getValue());
	}
	
	private void setNoteFilter() {
		reloadableList.noteFilter(noteFilter.getValue());
	}
	
	private void setGenreFilter() {
		reloadableList.genreFilter(genreFilter.getValue());
	}
	
	private void closeDialog() {
		this.close();
		reloadableList.reloadList(getFilterModes());
	}
	
	private FilterModes[] getFilterModes() {
		List<FilterModes> list = new ArrayList<FilterModes>();
		for(int i = 0; i < boxes.length; i++) {
			if(boxes[i].getValue())
				list.add(filterModes[i]);
		}
		FilterModes[] fms = new FilterModes[list.size()];
		for(int i = 0; i < list.size(); i++) {
			fms[i] = list.get(i);
		}
		return fms;
	}
}
