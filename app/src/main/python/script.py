import instaloader

def check_profile_exist(username):
    L = instaloader.Instaloader()
    try:
        profile = instaloader.Profile.from_username(L.context, username)
        return True
    except instaloader.ProfileNotExistsException:
        return False

def download(profile):
    L = instaloader.Instaloader()
    L.save_metadata = False
    L.post_metadata_txt_pattern = ""
    L.dirname_pattern = f"/sdcard/InstaLoaderApp/{profile}"
    curr = 0
    profileobj = instaloader.Profile.from_username(L.context, profile)
    for post in profileobj.get_posts():
        L.download_post(post, target="")
        curr += 1

    return curr

def post_count(username):
    L = instaloader.Instaloader()
    profile = instaloader.Profile.from_username(L.context, username)
    posts = profile.mediacount
    return posts

# for reference: https://github.com/instaloader/instaloader/issues/1851
def download_post_from_link(shortcode):
    L = instaloader.Instaloader()
    L.dirname_pattern = f"/sdcard/InstaLoaderApp/posts"
    post = instaloader.Post.from_shortcode(L.context, shortcode)
    L.download_post(post, target="")
