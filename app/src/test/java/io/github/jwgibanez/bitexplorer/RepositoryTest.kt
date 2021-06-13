package io.github.jwgibanez.bitexplorer

import io.github.jwgibanez.bitexplorer.di.ApiImpl
import org.hamcrest.MatcherAssert
import org.junit.Test

class RepositoryTest: ApiImpl() {

    @Test
    fun repositoryService_getRepositories() {
        val testObserver = bitbucketService.getRepositories().test()
        testObserver.awaitTerminalEvent()
        testObserver
            .assertNoErrors()
            .assertValue(check {
                MatcherAssert.assertThat("pagelen ${it.pagelen}: ${it.pagelen}", it.pagelen > 0)
                MatcherAssert.assertThat("values ${it.values.size}: ${it.values.size}", it.values.isNotEmpty())
                MatcherAssert.assertThat("pagelen ${it.next}: ${it.next}", !it.next.isNullOrEmpty())
            })
    }

    @Test
    fun repositoryService_getNext() {
        val testObserver = bitbucketService.getNext(
            "https://api.bitbucket.org/2.0/repositories?after=2011-09-03T12%3A33%3A16.028393%2B00%3A00"
        ).test()
        testObserver.awaitTerminalEvent()
        testObserver
            .assertNoErrors()
            .assertValue(check {
                MatcherAssert.assertThat("pagelen ${it.pagelen}: ${it.pagelen}", it.pagelen > 0)
                MatcherAssert.assertThat("values ${it.values.size}: ${it.values.size}", it.values.isNotEmpty())
                MatcherAssert.assertThat("pagelen ${it.next}: ${it.next}", !it.next.isNullOrEmpty())
            })
    }
}