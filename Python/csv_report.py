#!/usr/bin/env python

def main():
	try:
		fin = open('../data/test.csv')
	except:
		try:
			fin = open('test.csv')
		except:
			print('\nPlease make sure testing data named as "test.csv", either in current-working-directory, or sibling-directory of data/test.csv\n')
			return 1

	#------------------------------------------------------
	# dictionary with key of 'name'  and 
	# value of a list of 'number of event' and 'total time'
	#------------------------------------------------------
	records = dict()

	print('\n<<<<< Input  <<<<<<\n')

	for line in fin:

		print line.strip()

		name, time, loc = line.strip().split(',')

		if name not in records:
			records[name] = [1, float(time)]
		else:
			records[name][0] += 1
			records[name][1] += float(time)


	print('\n>>>>> Output >>>>>>\n') 
	#-----------------------
	# processing the records
	#-----------------------
	print('Name : # of Events : average Time')
	for name in records:
		aveTime = float(records[name][1]) / float(records[name][0])
		print(name + ":" + str(records[name][0]) + ":" +  str(aveTime))

	print('\r')

			

#-------------------------------
if __name__ == "__main__":
    main()
