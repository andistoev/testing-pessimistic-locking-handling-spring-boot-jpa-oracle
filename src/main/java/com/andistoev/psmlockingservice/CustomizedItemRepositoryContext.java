package com.andistoev.psmlockingservice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Slf4j
@RequiredArgsConstructor
public abstract class CustomizedItemRepositoryContext {

    private static final String UNSUPPORTED_METHOD_ERROR_MESSAGE = "Method invocation not allowed!";

    @Getter
    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutForTestsAtStartup: false}")
    private boolean requiredToSetLockTimeoutForTestsAtStartup;

    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutForEveryQuery: false}")
    private boolean requiredToSetLockTimeoutForEveryQuery;

    @Getter
    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutQueryHint: true}")
    private boolean requiredToSetLockTimeoutQueryHint;

    @Getter
    @Value("${concurrency.pessimisticLocking.delayAtTheEndOfTheQueryForPessimisticLockingTestingInMs: 200}")
    private long delayAtTheEndOfTheQueryForPessimisticLockingTestingInMs;

    @Getter
    @Value("${concurrency.pessimisticLocking.minimalPossibleLockTimeOutInMs: 0}")
    private long minimalPossibleLockTimeOutInMs;

    @Getter
    @Value("${concurrency.pessimisticLocking.lockTimeOutInMsForQueryGetItem: 5000}")
    private long lockTimeOutInMsForQueryGetItem;

    protected final EntityManager em;

    protected void setLockTimeout(long timeoutDurationInMs) {
        throw new UnsupportedOperationException(UNSUPPORTED_METHOD_ERROR_MESSAGE);
    }

    protected long getLockTimeout() {
        throw new UnsupportedOperationException(UNSUPPORTED_METHOD_ERROR_MESSAGE);
    }

    protected Query setLockTimeoutIfRequired(Query query) {
        if (requiredToSetLockTimeoutForEveryQuery) {
            log.info("... set lockTimeOut {} ms through native query ...", getLockTimeOutInMsForQueryGetItem());
            setLockTimeout(getLockTimeOutInMsForQueryGetItem());
        }

        if (requiredToSetLockTimeoutQueryHint) {
            log.info("... set lockTimeOut {} ms through query hint ...", getLockTimeOutInMsForQueryGetItem());
            query.setHint("javax.persistence.lock.timeout", String.valueOf(getLockTimeOutInMsForQueryGetItem()));
        }

        return query;
    }

    protected void insertArtificialDealyAtTheEndOfTheQueryForTestsOnly() {
        // for testing purposes only
    }
}
