package example.micronaut.request

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/request") // <1>
class RobotController(
    private val robotMother: RobotMother, // <2>
    private val robotFather: RobotFather
) {

    @Get
    fun children(): List<String> {
        return listOf(robotMother.child().getSerialNumber(), robotFather.child().getSerialNumber())
    }
}