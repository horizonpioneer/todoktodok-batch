package todoktodok.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataGenerationJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job dataGenerationJob;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== 데이터 생성 배치 작업 시작 ===");

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(dataGenerationJob, jobParameters);

        log.info("=== 데이터 생성 배치 작업 완료 ===");
    }
}