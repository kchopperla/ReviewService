package com.intuit.reviewengine.services;

import java.sql.Date;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Before;
import org.junit.Test;

import com.intuit.reviewengine.Utils.ServiceUtils;
import com.intuit.reviewengine.models.Review;
import com.intuit.reviewengine.repositories.ReviewRepository;
import com.intuit.reviewengine.resources.ReviewResource;
import com.intuit.reviewengine.services.impl.ReviewServiceImpl;

public class ReviewServiceUnitTest {

	private ReviewServiceImpl reviewService;

	private @Mocked ReviewRepository reviewRepository;
	
	@Before
	public void setUp() {
		reviewService = new ReviewServiceImpl();
		reviewService.setReviewRepository(reviewRepository);
	}

	/**
	 * This test should execute successful save operation on Review Repository.
	 * @throws Exception
	 */
	@Test
	public void testSave_success(final @Mocked ReviewResource reviewResource, final @Mocked Review reviewEntity, final @Mocked Date date) throws Exception {
		
		new NonStrictExpectations() {{
			reviewResource.getId();result=null;
			reviewResource.getCreatedDate();result=date;
			date.getTime();result=1L;
		}};
		
		reviewService.save(reviewResource);
		
		new Verifications() {{
			reviewRepository.save(withAny(reviewEntity)); times = 1;
		}};
	}
	
	/**
	 * This test should execute successful save operation on Review Repository.
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSave_error() throws Exception {
		reviewService.save(null);
	}
	
	/**
	 * This test should execute successful delete operation on a review.
	 * @param reviewResourceId
	 * @param review
	 * @throws Exception
	 */
	@Test
	public void testDelete_success(final @Mocked Integer reviewResourceId, final @Mocked Review review) throws Exception {
		
		new NonStrictExpectations() {{
			reviewRepository.findOne(reviewResourceId);result=review;
		}};
		
		reviewService.delete(100);
		
		new Verifications() {{
			reviewRepository.delete(withAny(review)); times = 1;
		}};
	}
	
	/**
	 * This test should error out on a delete operation on Review Repository
	 * when resource id to be deleted is null.
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testDelete_error() throws Exception {
		reviewService.delete(null);
	}
	
	@Test
	public void testFindReviewById_success(final @Mocked Integer id) throws Exception {
		
		new NonStrictExpectations() {{
		}};
		
		reviewService.findReviewById(100);
		
		new Verifications() {{
			reviewRepository.findOne(withAny(id)); times = 1;
		}};
	}
	
	/**
	 * This test should error out on a delete operation on Review Repository
	 * when resource id to be deleted is null.
	 * @throws Exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFindReviewById_error() throws Exception {
		reviewService.findReviewById(null);
	}
	
}