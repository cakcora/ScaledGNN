# ScaledGNN

## Data
**V0.1 and V0.2** store information (in a JSON string) on all the Twitter accounts followed by each US Congress member.\
**handles_party.txt** stores the official Twitter handles of all US Congress memebers and their associated parties.\
**V1.txt** contains the distinct Twitter IDs that are followed by at least one US Congress member, who follows at most **2000** Twitter users.

## WebScraping
An R script that scrapes [data from an HTML table](https://pressgallery.house.gov/member-data/members-official-twitter-handles) using the rvest package. This table contains the names of the US congress members, their twitter handles, state/district and the party they belong to.\
\
**Note:** The table in the above website is updated from time to time, hence members.R may need to be updated if new changes made to the website produce unexpected results.


## TwitterDataProject
**TwitterDataProject** is a collector to get the list of users that given Twitter users follow, it can be a US Congress member or users in V01. This program is design to get following list from Twitter API V2 Access level: Essential, which has limitation on request. This programs will return files named based on Twitter users' name that will search for following lists.

## CongressFollowingSet
The main purpose of this program is to get V1.txt. The constraint of **2000** users is due to the amount of time and resources it will take to query the IDs of all users that are followed by US Congress members who follow a large number of users. For example, at the time V0 was created, Congress member Eleanor Norton, followed 27999 users. V1.txt will be queried to get V2.txt, which will be the set of user IDs that are followed by at least one user in V1.txt.

We also print a few results such as the number of Democrats and Republicans amongst the US congress members, who follow greater than **2000** users, since their following list will not be queried. This information is useful because any imbalance or biases in the data must be taken into account during research.

