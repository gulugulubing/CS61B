public class HelloNumbers {
    public static void main(String[] args) {
        int i = 0;
		int j,sum;
        while (i < 10) {
            j = 0;
			sum = 0;
			while(j<i+1){
				sum = sum +j;
				j = j + 1;
			}
			i = i + 1;
			System.out.print(sum + " ");
        }
		System.out.println();
    }
}

