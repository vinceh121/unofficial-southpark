package me.vinceh121.unofficialsouthpark.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class MediaInfo implements Serializable {
	@JsonProperty("origination_date")
	private String originationDate;
	private List<Transcript> transcript;
	private List<Rendition> rendition;

	public String getOriginationDate() {
		return originationDate;
	}

	public void setOriginationDate(String originationDate) {
		this.originationDate = originationDate;
	}

	public List<Transcript> getTranscript() {
		return transcript;
	}

	public void setTranscript(List<Transcript> transcript) {
		this.transcript = transcript;
	}

	public List<Rendition> getRendition() {
		return rendition;
	}

	public void setRendition(List<Rendition> rendition) {
		this.rendition = rendition;
	}

	public static class Rendition implements Serializable {
		private String src, type, method, cdn;
		private long duration;
		private int rdcount;

		public String getSrc() {
			return src;
		}

		public void setSrc(String src) {
			this.src = src;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getCdn() {
			return cdn;
		}

		public void setCdn(String cdn) {
			this.cdn = cdn;
		}

		public long getDuration() {
			return duration;
		}

		public void setDuration(long duration) {
			this.duration = duration;
		}

		public int getRdcount() {
			return rdcount;
		}

		public void setRdcount(int rdcount) {
			this.rdcount = rdcount;
		}

		@Override
		public String toString() {
			return "Rendition{" +
					"src='" + src + '\'' +
					", type='" + type + '\'' +
					", method='" + method + '\'' +
					", cdn='" + cdn + '\'' +
					", duration=" + duration +
					", rdcount=" + rdcount +
					'}';
		}
	}

	public static class Transcript implements Serializable {
		private String srclang, label, kind;
		private List<Typographic> typographic;

		public String getSrclang() {
			return srclang;
		}

		public void setSrclang(String srclang) {
			this.srclang = srclang;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public List<Typographic> getTypographic() {
			return typographic;
		}

		public void setTypographic(final List<Typographic> typographic) {
			this.typographic = typographic;
		}

		@Override
		public String toString() {
			return "Transcript{" +
					"srclang='" + srclang + '\'' +
					", label='" + label + '\'' +
					", kind='" + kind + '\'' +
					", typographic=" + typographic +
					'}';
		}
	}

	public static class Typographic implements Serializable {
		private String format, src;

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getSrc() {
			return src;
		}

		public void setSrc(String src) {
			this.src = src;
		}

		@Override
		public String toString() {
			return "Typographic{" +
					"format='" + format + '\'' +
					", src='" + src + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "MediaInfo{" +
				"originationDate='" + originationDate + '\'' +
				", transcript=" + transcript +
				", rendition=" + rendition +
				'}';
	}
}
