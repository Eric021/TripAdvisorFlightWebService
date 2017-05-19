package com.tripAdvisorIR.merge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tripAdvisorIR.dbModel.Hotel;

public class MergeHotelsTest {

	/*
	 * how to use:
	 */
	// public static void main(String[] args){
	// Integer[] a = {2, 6, 3, 5, 1};
	// mergeSort(a);
	// System.out.println(Arrays.toString(a));
	// }
	/*
	 * end of how to use
	 */

	private List<Hotel> hotels = new ArrayList<>();

	public MergeHotelsTest() {

	}

	public MergeHotelsTest(List<Hotel> hotels) {
		this.hotels = hotels;
	}

	public void mergeSort(List<Hotel> a) {
		Hotel[] tmp = new Hotel[a.size()];
		mergeSort(a, tmp, 0, a.size() - 1);
	}

	private void mergeSort(List<Hotel> a, Hotel[] tmp, int left, int right) {
		if (left < right) {
			int center = (left + right) / 2;
			mergeSort(a, tmp, left, center);
			mergeSort(a, tmp, center + 1, right);
			merge(a, tmp, left, center + 1, right);
		}
	}

	private void merge(List<Hotel> a, Hotel[] tmp, int left, int right, int rightEnd) {
		int leftEnd = right - 1;
		int k = left;
		int num = rightEnd - left + 1;

		while (left <= leftEnd && right <= rightEnd)
			if (a.get(left).compareTo(a.get(right)) <= 0)
				tmp[k++] = a.get(left++);
			else
				tmp[k++] = a.get(right++);

		while (left <= leftEnd) // Copy rest of first half
			tmp[k++] = a.get(left++);

		while (right <= rightEnd) // Copy rest of right half
			tmp[k++] = a.get(right++);

		// Copy tmp back
		for (int i = 0; i < num; i++, rightEnd--)
			a.set(rightEnd, (Hotel) tmp[rightEnd]);

		for (Hotel hotel : a) {
			this.hotels.add(hotel);
		}
	}

	public List<Hotel> getMergedSortedHotels() {
		return this.hotels;
	}

}
