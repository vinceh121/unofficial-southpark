package me.vinceh121.unofficialsouthpark.entities;

import java.io.Serializable;
import java.util.List;

public class SPData implements Serializable {
	private String created;
	private List<List<Episode>> seasons;

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public List<List<Episode>> getSeasons() {
		return seasons;
	}

	public void setSeasons(List<List<Episode>> seasons) {
		this.seasons = seasons;
	}

	@Override
	public String toString() {
		return "SPData{" +
				"created=" + created +
				", seasons=" + seasons +
				'}';
	}
}
