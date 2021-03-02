package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxStreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.reactivex.Flowable
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest // <1>
class BintrayControllerSpec extends Specification {

    @Inject
    @Client("/")
    RxStreamingHttpClient client // <2>

    @Shared
    List<String> expectedProfileNames = ['base', 'federation', 'function', 'function-aws', 'service']

    def "Verify bintray packages can be fetched with low level HttpClient"() {
        when:
        HttpRequest request = HttpRequest.GET('/bintray/packages-lowlevel')

        HttpResponse<List<BintrayPackage>> rsp = client.toBlocking().exchange(request, // <3>
                Argument.listOf(BintrayPackage)) // <4>

        then: 'the endpoint can be accessed'
        rsp.status == HttpStatus.OK // <5>
        rsp.body() // <6>

        when:
        List<BintrayPackage> packages = rsp.body()

        then:
        for (String name : expectedProfileNames) {
            assert packages*.name.contains(name)
        }
    }

    def "Verify bintray packages can be fetched with compile-time autogenerated @Client"() {
        when:
        HttpRequest request = HttpRequest.GET('/bintray/packages')

        Flowable<BintrayPackage> bintrayPackageStream = client.jsonStream(request, BintrayPackage) // <7>
        Iterable<BintrayPackage> bintrayPackages = bintrayPackageStream.blockingIterable()

        then:
        for (String name : expectedProfileNames) {
            assert bintrayPackages*.name.contains(name)
        }
    }
}
