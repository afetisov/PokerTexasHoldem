package server.dao;

import commons.model.Hand;

public interface GameDAO {
	
	void saveHandHistory(Hand hand) throws Exception;
}
