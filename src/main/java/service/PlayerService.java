package service;

import java.io.File;

import org.apache.log4j.Logger;

import player.Player;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;
import events.DownloadFinished;
import events.PlayEvent;

public class PlayerService {
	
	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	private EventBus eventBus;
	private Player player;
	private PlayList playList;
		
	private void play(Song song) {
		player.prepareFor(song.getFile().getAbsolutePath());
		player.play();
	}

	@Subscribe
	synchronized public void playInitial(PlayEvent e){
		Song song = e.getSong();
		File dest = song.getFile();
		if(dest == null){
			eventBus.post(new DownloadEvent(song));
		} else {
			play(song);
		}
	}
	
	@Subscribe
	public void playAfterFinished(DownloadFinished e) {
		Song song = e.getSong();
		play(song);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayList getPlayList() {
		return playList;
	}

	public void setPlayList(PlayList playList) {
		this.playList = playList;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}