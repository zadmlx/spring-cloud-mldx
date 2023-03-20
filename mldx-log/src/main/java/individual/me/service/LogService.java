package individual.me.service;

import individual.me.domain.Log;

import java.util.List;

public interface LogService {
    /**
     * 添加日志
     * @param log
     */
    void insertLog(Log log);

    /**
     * 根据log日志类型删除日志，error和info
     * @param logType 日志类型
     */
    void deleteByType(String logType);

    /**
     * 根据日志类型查询
     * @param logType 日志类型
     */
    List<Log> selectByType(String logType);

    /**
     * 根据用户名查询日志
     * @param userId 用户id
     * @return
     */
    List<Log> selectByUserid(String userId);

    /**
     * 根据ids 批量删除
     * @param ids
     */
    void deleteByIds(Long[] ids);

}
