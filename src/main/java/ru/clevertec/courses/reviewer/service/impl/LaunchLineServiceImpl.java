package ru.clevertec.courses.reviewer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.courses.reviewer.entity.LaunchLine;
import ru.clevertec.courses.reviewer.repository.LaunchLineRepository;
import ru.clevertec.courses.reviewer.service.LaunchLineService;

import static ru.clevertec.courses.reviewer.constant.Constant.EMPTY_LINE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LaunchLineServiceImpl implements LaunchLineService {

    private final LaunchLineRepository launchLineRepository;

    @Override
    public String getArgsByLaunchLineId(Integer id) {
        return launchLineRepository.findById(id)
                .map(LaunchLine::getArguments)
                .orElse(EMPTY_LINE);
    }
}
