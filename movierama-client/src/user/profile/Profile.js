import React, { Component } from 'react';
import MovieList from '../../movie/MovieList';
import { getUserProfile } from '../../utils/APIClient';
import { Avatar, Tabs, notification } from 'antd';
import { getAvatarColor } from '../../utils/Colors';
import LoadingIndicator  from '../../common/LoadingIndicator';
import './profile.css';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';
import { withRouter } from 'react-router-dom';

const TabPane = Tabs.TabPane;

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false
        }
        this.loadUserProfile = this.loadUserProfile.bind(this);
        this.handleTotalMovieOpinions = this.handleTotalMovieOpinions.bind(this);
    }

    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
        .then(response => {
            this.setState({
                user: response,
                isLoading: false
            });
        }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else if(error.status === 401){
                this.props.history.push("/login");
                notification.info({
                    message: 'Movierama',
                    description: "Please login to see the details",          
                });      
            } 
            else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });        
            }
        });        
    }
      
    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
    }

    componentDidUpdate(nextProps) {
        if(this.props.match.params.username !== nextProps.match.params.username) {
            console.log("componentDidupdate laod nextProps.match.params.username")
            this.loadUserProfile(this.props.match.params.username);
        }        
    }

    handleTotalMovieOpinions = (data) => {
        if(data === this.state.user.name){
            var userValue = {...this.state.user}
            userValue.totalMovieOpinions = userValue.totalMovieOpinions - 1;
            this.setState({
                user: userValue
            });
        }
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }

        const tabBarStyle = {
            textAlign: 'center'
        };

        return (
            <div className="profile">
                { 
                    this.state.user ? (
                        <div className="user-profile">
                            <div className="user-details">
                                <div className="user-avatar">
                                    <Avatar className="user-avatar-circle" style={{ backgroundColor: getAvatarColor(this.state.user.name)}}>
                                        {this.state.user.name[0].toUpperCase()}
                                    </Avatar>
                                </div>
                                <div className="user-summary">
                                    <div className="full-name">{this.state.user.name}</div>
                                    <div className="username">@{this.state.user.username}</div>
                                </div>
                            </div>
                            <div className="user-movie-details">    
                                <Tabs defaultActiveKey="1" 
                                    animated={false}
                                    tabBarStyle={tabBarStyle}
                                    size="large"
                                    className="profile-tabs">
                                    <TabPane tab={`${this.state.user.totalCreatedMovies}  Movies`} key="1">
                                        <MovieList isAuthenticated={this.props.isAuthenticated} currentUser={this.props.currentUser} username={this.props.match.params.username} type="USER_CREATED_MOVIES" isProfileMovieList={true} handleTotalMovieOpinions={this.handleTotalMovieOpinions}/>
                                    </TabPane>
                                    <TabPane tab={`${this.state.user.totalMovieOpinions} Opinions`}  key="2">
                                        <MovieList isAuthenticated={this.props.isAuthenticated} currentUser={this.props.currentUser} username={this.props.match.params.username} type="USER_OPINIONS" isProfileMovieList={true} handleTotalMovieOpinions={this.handleTotalMovieOpinions}/>
                                    </TabPane>
                                </Tabs>
                            </div>  
                        </div>  
                    ): null               
                }
            </div>
        );
    }
}

export default withRouter(Profile);