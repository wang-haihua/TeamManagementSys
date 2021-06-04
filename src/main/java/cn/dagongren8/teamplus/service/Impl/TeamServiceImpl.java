package cn.dagongren8.teamplus.service.Impl;

import cn.dagongren8.teamplus.entity.Team;
import cn.dagongren8.teamplus.mapper.TeamMapper;
import cn.dagongren8.teamplus.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    @Override
    public List<Team> getAllTeams() {
        return teamMapper.getAllTeams();
    }

    @Override
    public Team getTeamById(int teamId) {
        return teamMapper.getTeamById(teamId);
    }

    @Override
    public Team getTeamByTeamIdentifier(String teamIdentifier) {
        return teamMapper.getTeamByTeamIdentifier(teamIdentifier);
    }

    @Override
    public int insertTeam(Team team) {
        return teamMapper.insertTeam(team);
    }

    @Override
    public int updateTeamById(Team team) {
        return teamMapper.updateTeamById(team);
    }

    @Override
    public int deleteTeamById(int teamId) {
        return teamMapper.deleteTeamById(teamId);
    }
}
