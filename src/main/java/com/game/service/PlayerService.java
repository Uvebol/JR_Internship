package com.game.service;

import java.util.*;

import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.PlayerNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public List<Player> findAll() {
        return (List<Player>) playerRepository.findAll();
    }

    @Transactional
    public Player create(Player player) {
        return playerRepository.save(player);
    }

    @Transactional
    public Player update(long id, Player player) throws PlayerNotFoundException {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if (!optionalPlayer.isPresent()) {
            throw new PlayerNotFoundException();
        }
        Player playerToUpdate = optionalPlayer.get();
        if (player.getName() != null) {
            playerToUpdate.setName(player.getName());
        }
        if (player.getTitle() != null) {
            playerToUpdate.setTitle(player.getTitle());
        }
        if (player.getRace() != null) {
            playerToUpdate.setRace(player.getRace());
        }
        if (player.getProfession() != null) {
            playerToUpdate.setProfession(player.getProfession());
        }
        if (player.getBirthday() != null) {
            playerToUpdate.setBirthday(player.getBirthday());
        }
        if (player.getBanned() != null) {
            playerToUpdate.setBanned(player.getBanned());
        }
        if (player.getExperience() != null) {
            playerToUpdate.setExperience(player.getExperience());
        }
        playerToUpdate.setLevel(countLevel(playerToUpdate));
        playerToUpdate.setUntilNextLevel(countUntilNextLevel(playerToUpdate));
        return playerRepository.save(playerToUpdate);
    }

    @Transactional
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    @Transactional
    public Player findById(Long id) {
        Optional<Player> player = playerRepository.findById(id);
        return player.get();
    }

    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    @Transactional
    public List<Player> findAllByParamsPageable(String name, String title, Race race, Profession profession,
                                                Date after, Date before, Boolean banned, Integer minExperience,
                                                Integer maxExperience, Integer minLevel, Integer maxLevel,
                                                Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return playerRepository.findAllByParams(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel,
                maxLevel, pageable);
    }

    @Transactional
    public int findAllByParamsAndCount(String name, String title, Race race, Profession profession,
                                       Date after, Date before, Boolean banned, Integer minExperience,
                                       Integer maxExperience, Integer minLevel, Integer maxLevel) {
        List<Player> all = playerRepository.findAllByParams(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel);
        return all.size();
    }

    public boolean checkPlayerBeforeSave(Player player) {
        long begin = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTimeInMillis(); //TODO
        long end = new GregorianCalendar(3000, Calendar.JANUARY, 1).getTimeInMillis();
        long birtDay = player.getBirthday().getTime();
        return player.getName() != null && !player.getName().isEmpty() && player.getName().length() <= 12
                && player.getTitle().length() <= 30 && player.getTitle() != null && !player.getTitle().isEmpty()
                && player.getExperience() > -1 && player.getExperience() <= 10000000
                && player.getRace() != null && player.getProfession() != null && player.getBirthday() != null
                && birtDay > -1 && (birtDay >= begin && birtDay <= end);
    }

    public boolean checkBirthday(Date date) {
        if (date == null) {
            return true;
        }
        long birtDay = date.getTime();
        long begin = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTimeInMillis();
        long end = new GregorianCalendar(3000, Calendar.JANUARY, 1).getTimeInMillis();

        return birtDay >= 0 && (birtDay >= begin && birtDay <= end);
    }

    public boolean checkExperience(Integer exp) {
        if (exp == null) {
            return true;
        }
        return exp >= 0 && exp <= 10_000_000;
    }

    public int countLevel(Player player) {
        return (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
    }

    public int countUntilNextLevel(Player player) {
        return 50 * (countLevel(player) + 1) * (countLevel(player) + 2) - player.getExperience();
    }

    public boolean isValidId(String playerId) {
        long id;
        try {
            id = Long.parseLong(playerId);
        } catch (NumberFormatException e) {
            return false;
        }
        return id > 0;
    }

    public boolean isEmptyBody(Player player) {
        return player.getName() == null
                && player.getTitle() == null
                && player.getRace() == null
                && player.getProfession() == null
                && player.getBirthday() == null
                && player.getBanned() == null;
    }
}