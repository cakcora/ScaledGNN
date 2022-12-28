
#-------------------------------------------------------------------
# Purpose: Web scraping html table of US Congressmen
#-------------------------------------------------------------------

rm(list = ls())

# Install pacman
if (!require("pacman")) install.packages("pacman")

# load packages
pacman::p_load(rvest,
               dplyr,
               stringr)
               
# Web scraping
website_url = "https://pressgallery.house.gov/member-data/members-official-twitter-handles"
website = read_html(website_url)

tbls = html_nodes(website, "table")

tbls_ls = website %>%
  html_nodes("table") %>%
  .[1] %>%
  html_table(fill = TRUE)

# Manipulate data
congress_tbl = tbls_ls %>%
  as.data.frame() %>%
  slice(-(1:2)) %>%
  rename(first_name = X1,
         last_name = X2,
         handle = X3,
         state_dist = X4,
         party = X5 )

handles = congress_tbl %>%
  filter(handle != "TBD", handle != "No official") %>%
  select(handle) %>%
  mutate(handle = str_sub(handle, 2, length(handle)))

# Save list of member handles to .txt file
file_date = format(Sys.Date(), "%Y%m%d")
file = paste0("./handles_", file_date , ".txt")

handles$handle %>%
  writeLines(file)



