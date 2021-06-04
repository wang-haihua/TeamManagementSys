package cn.dagongren8.teamplus.mapper;

import cn.dagongren8.teamplus.entity.Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wanghaihua
 * @since 2021-01-12
 */

@Mapper
public interface TeamMapper {

    List<Team> getAllTeams();

    Team getTeamById(int teamId);

    Team getTeamByTeamIdentifier(String teamIdentifier);

    int insertTeam(Team team);

    int updateTeamById(Team team);

    int deleteTeamById(int teamId);

}
