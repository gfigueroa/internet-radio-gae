/**
 * Returns the location of the current URL.
 * This includes protocol + hostname + port (if any)
 * For example: http://localhost:8888
 * @return the location of the current URL
 */
function getURLLocation() {
	return location.protocol + '//' + location.hostname + (location.port ? ':' + location.port: '');
}