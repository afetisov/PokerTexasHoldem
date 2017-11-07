package commons.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "SESSIONS")
@org.hibernate.annotations.Table(appliesTo = "SESSIONS", indexes = { @Index(name = "idx_sessions_player_id", columnNames = {"player_id"}) })
@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
public class Session implements Serializable {
	
	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@Column(length = 50)
	private String id;
	
	@OneToOne
	@JoinColumn(name = "player_id", unique = true)
	@ForeignKey(name = "fk_player_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Player player;
	
	private Date createDate = new Date();
	
	private Date updateDate;
	
	public Session() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Session) {
		
			Session other = (Session) obj;
			
			if(id == null || other.id == null) return false;
			
			return id.equals(other.id);
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		
		if(id != null) return id.hashCode();
		
		return super.hashCode();
	}
}
