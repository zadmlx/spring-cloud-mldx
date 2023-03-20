package individual.me.service.impl;

import individual.me.domain.Log;
import individual.me.repository.LogRepository;
import individual.me.service.LogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Async
    @Override
    public void insertLog(Log log) {
        logRepository.insertLog(log);
    }

    @Override
    public void deleteByType(String logType) {
        logRepository.deleteByType(logType);
    }

    @Override
    public List<Log> selectByType(String logType) {
        return logRepository.selectByType(logType);
    }

    @Override
    public List<Log> selectByUserid(String userId) {
        return logRepository.selectByUserid(userId);
    }

    @Override
    public void deleteByIds(Long[] ids) {
        logRepository.deleteByIds(ids);
    }
}
