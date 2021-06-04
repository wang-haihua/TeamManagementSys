package cn.dagongren8.teamplus.service;

import cn.dagongren8.teamplus.entity.Team;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public interface TeamService {
    List<Team> getAllTeams();

    Team getTeamById(int teamId);

    Team getTeamByTeamIdentifier(String teamIdentifier);

    int insertTeam(Team team);

    int updateTeamById(Team team);

    int deleteTeamById(int teamId);
}
