/*
 *
 * Copyright 2018 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package uk.ac.ebi.ampt2d.accession.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uk.ac.ebi.ampt2d.accession.ApplicationConstants;
import uk.ac.ebi.ampt2d.accession.study.persistence.StudyAccessioningDatabaseService;
import uk.ac.ebi.ampt2d.accession.study.persistence.StudyAccessioningRepository;
import uk.ac.ebi.ampt2d.commons.accession.autoconfigure.EnableSpringDataContiguousIdService;
import uk.ac.ebi.ampt2d.commons.accession.generators.DecoratedAccessionGenerator;
import uk.ac.ebi.ampt2d.commons.accession.generators.monotonic.MonotonicAccessionGenerator;
import uk.ac.ebi.ampt2d.commons.accession.persistence.monotonic.service.ContiguousIdBlockService;

@Configuration
@ConditionalOnProperty(name = "services", havingValue = "study-accession")
@EnableSpringDataContiguousIdService
@EntityScan({"uk.ac.ebi.ampt2d.accession.study.persistence"})
@EnableJpaRepositories(
        basePackages = {"uk.ac.ebi.ampt2d.accession.study.persistence"}
)
public class StudyConfiguration {

    @Value("${" + ApplicationConstants.STUDY_BLOCK_SIZE + "}")
    private long blockSize;

    @Value("${" + ApplicationConstants.STUDY_ID + "}")
    private String studyId;

    @Value("${" + ApplicationConstants.APPLICATION_INSTANCE_ID + "}")
    private String applicationInstanceId;

    @Autowired
    private ContiguousIdBlockService service;

    @Autowired
    private StudyAccessioningRepository repository;

    @Bean
    public StudyAccessioningService studyAccessionService() {
        return new StudyAccessioningService(studyAccessionGenerator(), studyAccessioningDatabaseService());
    }

    @Bean
    public StudyAccessioningDatabaseService studyAccessioningDatabaseService() {
        return new StudyAccessioningDatabaseService(repository);
    }

    @Bean
    public DecoratedAccessionGenerator<StudyModel, Long> studyAccessionGenerator() {
        return DecoratedAccessionGenerator.buildPrefixSuffixMonotonicAccessionGenerator(new MonotonicAccessionGenerator<>(blockSize, studyId, applicationInstanceId, service),
                "STUDY", "");
    }
}
