
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	// Utilizar launcher inquirer para comprobar que las query devuelven los datos que queremos

	@Query("SELECT min(l.scheduledDeparture) FROM Leg l WHERE l.flight.id = :flightId")
	Date findScheduledDeparture(@Param("flightId") int flightId);

	@Query("SELECT max(l.scheduledArrival) FROM Leg l WHERE l.flight.id = :flightId")
	Date findScheduledArrival(@Param("flightId") int flightId);

	@Query("SELECT l.departureAirport FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture asc")
	String findOriginCity(@Param("flightId") int flightId);

	@Query("SELECT l.arrivalAirport FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival asc")
	String findDestinationCity(@Param("flightId") int flightId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.id = :flightId")
	Integer countLegs(@Param("flightId") int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	List<Leg> findLegsByFlight(@Param("flightId") int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber")
	Leg findLegByFlightNumber(@Param("flightNumber") String flightNumber);

}
