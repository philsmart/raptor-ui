package uk.ac.cardiff.raptor.ui.common;

import org.springframework.beans.factory.DisposableBean;

/**
 * Interface to extend if the component is expected to have initialise and
 * destroy methods, but those methods are not specific to an IOC container e.g
 * spring beans InitializingBean and {@link DisposableBean}.
 * 
 * @author philsmart
 *
 */
public interface InitialiseDestroyComponent {

	/**
	 * Logic to initalise a component after properties are set.
	 */
	void init();

	/**
	 * Logic to perform before an object is destroyed or disposed.
	 */
	void destroy();

}
