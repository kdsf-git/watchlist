package server.watchlist.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("/search")
public class SearchListView extends ListView implements HasUrlParameter<String> {
	private String searchString;
	
	@Override
	protected void addResponseObjectLayouts() {
		//left empty on purpose
	}
	
	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		for(int i = 0; i < responseObjectLayouts.size(); i++) {
			try {
				if(!(responseObjectLayouts.get(i).getResponseObject().data.media.title.english.toLowerCase().contains(parameter) || responseObjectLayouts.get(i).getResponseObject().data.media.title.romaji.toLowerCase().contains(parameter))) {
					responseObjectLayouts.remove(i);
					i--;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < responseObjectLayouts.size(); i++) {
			if(i % 2 == 0)
				responseObjectLayouts.get(i).getStyle().set("background-color", "#f0f0f0");
			responseObjectLayouts.get(i).getStyle().set("border-radius", "1em");
			this.add(responseObjectLayouts.get(i));
		}
	}
}
