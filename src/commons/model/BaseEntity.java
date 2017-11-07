package commons.model;

import java.io.Serializable;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BaseEntity) {
			BaseEntity that = (BaseEntity) obj;
			return this.id == null? super.equals(obj) : that.id == this.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		
		if(id != null) return id.hashCode();
		
		return super.hashCode();
	}
	
	
}
