package commons.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "PASSWORDS")
public class PlayerPassword implements Serializable {
	
	@Id
	private Long id; 

	@MapsId 
	@OneToOne
	@JoinColumn(name = "player_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Player player;
	
	@Column(length = 50)
	private String value;

	private Date lastModified = new Date();
	
	public PlayerPassword() {}
	
	public PlayerPassword(String password) {
		this.value = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String password) {
		this.value = password;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
