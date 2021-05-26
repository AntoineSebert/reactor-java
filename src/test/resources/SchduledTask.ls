Target Java;

reactor startupTest {
	Timer t: (1000000000 nsec,1000000000 nsec);

	reaction (t) {
		printf("Main reactor executed after %lld.\n", get_elapsed_logical_time());
	}
}

main reactor Minimal {

	reaction (startup) {
		printf("reactor executed after %lld.\n", get_elapsed_logical_time());
	}
}
