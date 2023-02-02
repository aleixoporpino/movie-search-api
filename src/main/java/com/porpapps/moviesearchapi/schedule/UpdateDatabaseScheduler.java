/*
package com.porpapps.moviestreamingsearchservice.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.porpapps.moviestreamingsearchservice.model.Movie;
import com.porpapps.moviestreamingsearchservice.model.TvShow;
import com.porpapps.moviestreamingsearchservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

@RequiredArgsConstructor
@Service
@EnableScheduling
public class UpdateDatabaseScheduler {

    private final Logger LOG = LoggerFactory.getLogger(UpdateDatabaseScheduler.class);

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int BATCH_SIZE;

    private final String MOVIE_DB_FILE_PATH = "http://files.tmdb.org/p/exports/";

    @Scheduled(cron = "0 0 0 * * TUE", zone = "America/Toronto")
    public void updateMovieTable() {
        final var stopWatch = new StopWatch();
        stopWatch.start();
        LOG.info("Started update movie table schedule");

        final var yesterday = LocalDate.now().minusDays(1);
        final var outputFileName = "movie_ids_" +
            StringUtils.leftPad(yesterday.getMonthValue() + "", 2, "0") + "_" +
            StringUtils.leftPad(yesterday.getDayOfMonth() + "", 2, "0") + "_" +
            yesterday.getYear() +
            ".json";
        final var gzipFilename = outputFileName + ".gz";

        downloadFile(gzipFilename);
        unzipGzipFileTo(gzipFilename, outputFileName);

        try (BufferedReader br = new BufferedReader(new FileReader(outputFileName))) {
            String line;
            var saved = 0;
            var count = 0;

            final var initialSql = "INSERT INTO moviesdb.movie (id, original_title) VALUES ";
            var insertQuery = new StringBuilder(initialSql);
            while ((line = br.readLine()) != null) {
                saved++;
                count++;
                final var movie = new ObjectMapper().readValue(line, Movie.class);

                insertQuery.append("(")
                    .append(movie.getId())
                    .append(", '")
                    .append(movie.getOriginalTitle().replaceAll("'", "''"))
                    .append("'), ");

                if (count >= BATCH_SIZE) {
                    insertQuery.deleteCharAt(insertQuery.lastIndexOf(","));
                    insertQuery.append(" ON CONFLICT (id) DO NOTHING;");

                    jdbcTemplate.execute(String.valueOf(insertQuery));

                    insertQuery = new StringBuilder(initialSql);
                    count = 0;
                    LOG.info("Persisted " + saved + " rows");
                }
            }

            if (count > 0) {
                insertQuery.deleteCharAt(insertQuery.lastIndexOf(","));
                insertQuery.append(" ON CONFLICT (id) DO NOTHING;");
                jdbcTemplate.execute(String.valueOf(insertQuery));
            }

            LOG.info("Finish persisting " + saved + " rows");
        } catch (IOException e) {
            LOG.error("Error when reading json file and persisting", e);
        } finally {
            final var gzipFile = new File(gzipFilename);
            final var jsonFile = new File(outputFileName);
            gzipFile.deleteOnExit();
            jsonFile.deleteOnExit();
        }
        stopWatch.stop();
        LOG.info("Finished update movies schedule in " + stopWatch.getTime(TimeUnit.MINUTES) + " minutes");
    }

    @Scheduled(cron = "0 0 0 * * TUE", zone = "America/Toronto")
    public void updateTvShowTable() {
        final var stopWatch = new StopWatch();
        stopWatch.start();
        LOG.info("Started update tv show table schedule");

        final var yesterday = LocalDate.now().minusDays(1);
        final var outputFileName = "tv_series_ids_" +
            StringUtils.leftPad(yesterday.getMonthValue() + "", 2, "0") + "_" +
            StringUtils.leftPad(yesterday.getDayOfMonth() + "", 2, "0") + "_" +
            yesterday.getYear() +
            ".json";
        final var gzipFilename = outputFileName + ".gz";

        downloadFile(gzipFilename);
        unzipGzipFileTo(gzipFilename, outputFileName);

        try (BufferedReader br = new BufferedReader(new FileReader(outputFileName))) {
            String line;
            var saved = 0;
            var count = 0;

            final var initialSql = "INSERT INTO moviesdb.tv_show (id, original_name) VALUES ";
            var insertQuery = new StringBuilder(initialSql);
            while ((line = br.readLine()) != null) {
                saved++;
                count++;
                final var tvShow = new ObjectMapper().readValue(line, TvShow.class);

                insertQuery.append("(")
                    .append(tvShow.getId())
                    .append(", '")
                    .append(tvShow.getOriginalName().replaceAll("'", "''"))
                    .append("'), ");

                if (count >= BATCH_SIZE) {
                    insertQuery.deleteCharAt(insertQuery.lastIndexOf(","));
                    insertQuery.append(" ON CONFLICT (id) DO NOTHING; ");

                    jdbcTemplate.execute(String.valueOf(insertQuery));

                    insertQuery = new StringBuilder(initialSql);
                    count = 0;
                    LOG.info("Persisted " + saved + " rows");
                }
            }

            if (count > 0) {
                insertQuery.deleteCharAt(insertQuery.lastIndexOf(","));
                insertQuery.append(" ON CONFLICT (id) DO NOTHING; ");
                jdbcTemplate.execute(String.valueOf(insertQuery));
            }

            LOG.info("Finish persisting " + saved + " rows");
        } catch (IOException e) {
            LOG.error("Error when reading json file and persisting", e);
        } finally {
            final var gzipFile = new File(gzipFilename);
            final var jsonFile = new File(outputFileName);
            gzipFile.deleteOnExit();
            jsonFile.deleteOnExit();
        }
        stopWatch.stop();
        LOG.info("Finished update tv show schedule in " + stopWatch.getTime(TimeUnit.MINUTES) + " minutes");
    }

    private void downloadFile(String filename) {
        try (final var in = new BufferedInputStream(new URL(MOVIE_DB_FILE_PATH + filename).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            final var dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOG.error("Error when downloading the gzip file", e);
        }

    }

    private void unzipGzipFileTo(String filename, String outputFileName) {
        try (final var gis = new GZIPInputStream(new FileInputStream(filename));

             final var fos = new FileOutputStream(outputFileName)) {

            // copy GZIPInputStream to FileOutputStream
            final var buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            LOG.error("File not found: " + filename, e);
        } catch (IOException e) {
            LOG.error("Error when unzipping file: " + filename, e);
        }
    }
}
*/
