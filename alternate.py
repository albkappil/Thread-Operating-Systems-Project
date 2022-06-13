import threading

# Imagine a door that allows only one person to go through at a time.
# There are two directions leading to the door, one from the left and one form the right.
# There is an equal number of people coming from both sides.
# To be fair the two sides must take turns. One from the left, one from the right, etc.

gWaitLeft = threading.Semaphore(1)
gWaitRight = threading.Semaphore(0)

def left(id):
    global gWaitLeft,gWaitRight
    gWaitLeft.acquire()
    print("Left Thread " + str(id) + " went through the door.")
    gWaitRight.release()

def right(id):
    global gWaitLeft,gWaitRight
    gWaitRight.acquire()
    print("Right Thread " + str(id) + " went through the door.")
    gWaitLeft.release()

for i in range(0,5):
    t = threading.Thread(target=left,args=(i,))
    t.start()

for i in range(0,5):
    t = threading.Thread(target=right,args=(i,))
    t.start()
