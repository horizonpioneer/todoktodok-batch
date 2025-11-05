package todoktodok.batch.job;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataGenerationJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    @Value("${spring.batch.job.name:allDataGenerationJob}")
    private String jobNames;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== 데이터 생성 배치 작업 시작 ===");
        log.info("실행할 Job: {}", jobNames);

        String[] jobNameArray = jobNames.split(",");
        Map<String, Job> jobs = applicationContext.getBeansOfType(Job.class);

        for (String jobName : jobNameArray) {
            String trimmedJobName = jobName.trim();

            if (jobs.containsKey(trimmedJobName)) {
                log.info(">>> Job 실행 시작: {}", trimmedJobName);

                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("timestamp", System.currentTimeMillis())
                        .addString("jobName", trimmedJobName)
                        .toJobParameters();

                jobLauncher.run(jobs.get(trimmedJobName), jobParameters);

                log.info("<<< Job 실행 완료: {}", trimmedJobName);
            } else {
                log.warn("Job을 찾을 수 없습니다: {}", trimmedJobName);
                log.info("사용 가능한 Job 목록: {}", jobs.keySet());
            }
        }

        log.info("=== 데이터 생성 배치 작업 완료 ===");
    }
}